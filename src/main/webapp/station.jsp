<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Station</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/body_top.jspf" %>

    <div class="ms-5 h3 _main-color1">
        Station
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
            <i class="fa fa-info-circle fa-2xl me-2"></i>
                ${messages}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>

        <c:set var="messages" value="${null}" scope="session" />
    </c:if>

    <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color2 p-3 shadow-lg me-5"
          method="POST" >

        <div class="mb-1">
            <label for="name" class="form-label">id</label>
            <input type="text" class="form-control" aria-label="id" id="id" name="id" disabled
                   value="${station.getId()}">
        </div>

        <div class="mb-1">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" placeholder="Station name" aria-label="name" id="name" name="name"
                   value="${station.getName()}">
        </div>

        <div class="form-group mt-3">
            <button type="submit" class="btn">Save</button>
            <a class="btn btn-outline-secondary" href="/stations/delete/${station.getId()}">Delete</a>
        </div>

    </form>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>