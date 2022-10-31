<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="stations.station" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/>
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
              method="POST">

            <div class="mb-1">
                <label for="name" class="form-label">id</label>
                <input type="text" class="form-control" aria-label="id"
                       id="id" name="id" disabled
                       value="${station.getId()}">
            </div>

            <div class="mb-1">
                <label for="name" class="form-label"><fmt:message key='stations.name'/></label>
                <input type="text" class="form-control"
                       value="${station.getName()}"
                       aria-label="name" id="name" name="name"
                       placeholder=
                <fmt:message key='stations.name'/>>
            </div>

            <div class="form-group mt-3">
                <button type="submit" class="btn" <customTags:access role="user" modifier="disabled"/>>
                    <fmt:message key='actions.save'/>
                </button>
                <a class="btn btn-outline-secondary <customTags:access role="user" modifier="disabled"/>"
                   href="/stations/delete/${station.getId()}">
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