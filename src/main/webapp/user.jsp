<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="users.user" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/> (${action})
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <c:if test="${not empty messages}">
        <div class="alert alert-light m-5 shadow alert-dismissible fade show" role="alert">
            <em class="fa fa-info-circle fa-2xl me-2"></em>
                ${messages}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>

        <c:set var="messages" value="${null}" scope="session"/>
    </c:if>

    <div class="d-flex justify-content-center">

        <%-- FORM --%>
        <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color2 p-3 shadow-lg me-5"
              method="POST" enctype="multipart/form-data">

            <div class="mb-1">
                <label for="id" class="form-label">id</label>
                <input type="text" class="form-control" aria-label="id"
                       id="id" name="id" readonly
                       value="${user.getId()}">
            </div>

            <div class="mb-1">
                <label for="name" class="form-label">
                    <fmt:message key='users.name'/>
                </label>
                <input type="text" class="form-control"
                       aria-label="name" id="name" name="name"
                       placeholder=
                       <fmt:message key='users.name'/>
                               value="${user.getName()}">

                <label for="password" class="form-label mt-1">
                    <fmt:message key='users.password'/>
                </label>
                <input type="password" class="form-control"
                       id="password" name="password" value="" aria-label="password"
                       placeholder=
                <fmt:message key='users.password'/>>

                <label for="email" class="form-label mt-1">E-mail</label>
                <input type="email" class="form-control" placeholder="E-mail" aria-label="email"
                       id="email" name="email" value="${user.getEmail()}">

                <div class="form-check mt-3">
                    <input class="form-check-input" type="checkbox" value="" id="isadmin"
                           <c:if test="${user.getIsAdmin()}">checked</c:if>
                           <c:if test="${user.getId() eq 1}">disabled</c:if>
                    <customTags:access role="user" modifier="disabled"/>>
                    <label class="form-check-label" for="isadmin">
                        <fmt:message key='users.admin'/>
                    </label>
                </div>

                <div class="form-check mt-3">
                    <input class="form-check-input" type="checkbox" value="" id="isactive"
                           <c:if test="${user.getIsActive()}">checked</c:if>>
                    <label class="form-check-label" for="isactive">
                        <fmt:message key='users.active'/>
                    </label>
                </div>

                <label for="activation_token" class="form-label mt-3">
                    <fmt:message key='users.activation_token'/>
                </label>
                <input type="activation_token" class="form-control" aria-label="activation_token"
                       id="activation_token" name="activation_token"
                       placeholder=
                       <fmt:message key='users.activation_token'/>
                               value="${user.getActivationToken()}">

            </div>

            <%--            <form action="upload" method="post" enctype="multipart/form-data">--%>
            <%--        <div class="form-group mt-3">--%>
            <%--            <img src="/images/image.jpg" alt="avatar" style="width: 150px">--%>
            <%--            <input type="file" name="file" value="asdfasdfasd" />--%>
            <%--        </div>--%>

            <div class="form-group mt-3">
                <button type="submit" class="btn" <customTags:access role="user" modifier="disabled"/>>
                    <fmt:message key='actions.save'/>
                </button>
                <a class="btn btn-outline-secondary <customTags:access role="user" modifier="disabled"/>"
                   href="/users/delete/${user.getId()}">
                    <fmt:message key='actions.delete'/>
                </a>
            </div>

        </form>
    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>