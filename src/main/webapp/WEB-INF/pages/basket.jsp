<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Prog.kiev.ua</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h3>Basket List</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <td><b>Photo</b></td>
            <td><b>Name</b></td>
            <td><b>Short Desc</b></td>
            <td><b>Long Desc</b></td>
            <td><b>Phone</b></td>
            <td><b>Price</b></td>
            <td><b>Action</b></td>
        </tr>
        </thead>
        <form class="form-inline" role="form" action="/SpringMVC_war_exploded/restore_or_deleteForever" method="post">
            <c:forEach items="${advs}" var="adv">
                <tr>
                    <td><img height="40" width="40" src="/SpringMVC_war_exploded/image/${adv.photo.id}"/></td>
                    <td>${adv.name}</td>
                    <td>${adv.shortDesc}</td>
                    <td>${adv.longDesc}</td>
                    <td>${adv.phone}</td>
                    <td>${adv.price}</td>
                    <td><input type="checkbox" onclick="check()" name="id[]" value=${adv.id}></td>
                </tr>
            </c:forEach>
            <td><input type="submit" name="name"  id="delButton" disabled  value="Restore_Selected"></td>
            <td><input type="submit" name="name"  id="delButton1" disabled  value="Delete_Selected_forever"></td>
        </form>
    </table>
    <h3>Return to Advertisements List</h3>
    <form class="form-inline" role="form" action="/SpringMVC_war_exploded/ " method="post">
        <td> <input type="submit" class="btn btn-default" value="Return"> </td>
    </form>
     <script>
        function check(){
            var cb = document.getElementsByTagName('input'),
                L = cb.length-1,
                f=true;
            for(;L>=0;L--){
                if (cb[L]['type']=='checkbox' && cb[L]['checked']==true){
                    f=!f; break;
                }
            }
            document.getElementById('delButton').disabled = f;
            document.getElementById('delButton1').disabled = f;
        }
    </script>
</div>
</body>
</html>