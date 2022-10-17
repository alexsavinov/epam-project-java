<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - User</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="ms-5 h3 _main-color1">
        User
        <c:choose>
            <c:when test="${action == 'add'}">
                (add)
            </c:when>
            <c:when test="${action == 'edit'}">
                (edit)
            </c:when>
            <c:otherwise>
                (view)
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty messages}">
        <div class="alert alert-light m-5 shadow alert-dismissible fade show" role="alert">
            <em class="fa fa-info-circle fa-2xl me-2"></em>
                ${messages}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>

        <c:set var="messages" value="${null}" scope="session"/>
    </c:if>

    <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color2 p-3 shadow-lg me-5"
          method="POST" enctype="multipart/form-data">

        <div class="mb-1">
            <label for="id" class="form-label">id</label>
            <input type="text" class="form-control" aria-label="id" id="id" name="id" readonly
                   value="${user.getId()}">
        </div>

        <div class="mb-1">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" placeholder="Username" aria-label="name" id="name" name="name"
                   value="${user.getName()}">

            <label for="password" class="form-label mt-1">Password</label>
            <input type="password" class="form-control" placeholder="Password" aria-label="name" id="password"
                   name="password" value="">

            <div class="form-check mt-3">

                <input class="form-check-input" type="checkbox" value="" id="isadmin"
                       <c:if test="${user.getIsAdmin()}">checked</c:if> disabled>
                <label class="form-check-label" for="isadmin">
                    Admin
                </label>
            </div>
            <%--            <input type="text" class="form-control" placeholder="Username" aria-label="name" id="password" name="password"--%>
            <%--                   value="${user.getPassword()}">--%>



<%--            <form action="upload" method="post" enctype="multipart/form-data">--%>

        </div>

<%--        <div class="form-group mt-3">--%>
<%--            <img src="/images/image.jpg" alt="avatar" style="width: 150px">--%>
<%--            <input type="file" name="file" value="asdfasdfasd" />--%>
<%--        </div>--%>

        <div class="form-group mt-3">
            <button type="submit" class="btn">Save</button>
            <a class="btn btn-outline-secondary" href="/users/delete/${user.getId()}">Delete</a>
        </div>

    </form>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>