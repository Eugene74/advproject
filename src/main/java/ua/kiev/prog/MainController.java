package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/SpringMVC_war_exploded")
public class MainController {

	@Autowired
	private AdvDAO advDAO;

	@RequestMapping("/")
	public ModelAndView listAdvs() {
		return new ModelAndView("index", "advs", advDAO.list());
	}

	@RequestMapping("/basket")
	public ModelAndView listBasket() {
		return new ModelAndView("basket", "advs", advDAO.listDeleted());
	}

	@RequestMapping(value = "/add_page", method = RequestMethod.POST)
	public String addPage(Model model) {
		return "add_page";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(@RequestParam(value="pattern") String pattern) {
		return new ModelAndView("index", "advs", advDAO.list(pattern));
	}

	@RequestMapping("/delete")
	public ModelAndView delete(@RequestParam(value="id") long id) {
				Advertisement adv=advDAO.getAdvById(id);
				adv.setBasket("deleted");
				advDAO.add(adv);
		return new ModelAndView("index", "advs", advDAO.list());
	}

	@RequestMapping("/image/{file_id}")
	public void getFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("file_id") long fileId) {
		try {
			byte[] content = advDAO.getPhoto(fileId);
			response.setContentType("image/png");
			response.getOutputStream().write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addAdv(@RequestParam(value="name") String name,
						 @RequestParam(value="shortDesc") String shortDesc,
						 @RequestParam(value="longDesc", required=false) String longDesc,
						 @RequestParam(value="phone") String phone,
						 @RequestParam(value="price") double price,
						 @RequestParam(value="photo") MultipartFile photo,
						 HttpServletRequest request,
						 HttpServletResponse response)
	{
		try {
			Advertisement adv = new Advertisement(
					name, shortDesc, longDesc, phone, price,
					photo.isEmpty() ? null : new Photo(photo.getOriginalFilename(), photo.getBytes())
			);
			advDAO.add(adv);
			return new ModelAndView("index", "advs", advDAO.list());
		} catch (IOException ex) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(value = "/deleteSelected", method = RequestMethod.POST)
	public ModelAndView deleteAdv(
						       @RequestParam(value="id[]") List<Long> id  )
	{
			for (Long d:id ) {
				Advertisement adv=advDAO.getAdvById(d);
				adv.setBasket("deleted");
				advDAO.add(adv);
			}
		return new ModelAndView("basket", "advs", advDAO.listDeleted());
	}
	@RequestMapping(value = "/restore_or_deleteForever", method = RequestMethod.POST)
	public ModelAndView restore_or_deleteForever(
			@RequestParam(value="id[]") List<Long> id,
			@RequestParam(value = "name")String s
	  )
	{
		 if(s.equals("Delete_Selected_forever")){
			 for (Long d : id) {
				 advDAO.delete(d);// это метод удаляет навсегда
			 }
		 }else {

				 for (Long d : id) {
					 Advertisement adv = advDAO.getAdvById(d);
					 adv.setBasket("active");
					 advDAO.add(adv);
				 }
		 }


		return new ModelAndView("index", "advs", advDAO.list());
	}
}