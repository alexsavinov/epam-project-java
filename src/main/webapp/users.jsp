<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Users" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - ${page_title}</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1 <customTags:access role="user" modifier="d-none"/>"
           href="/users/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            Add ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-3" aria-describedby="Users">
            <thead class="table-secondary">
            <tr>
                <th scope="col">id</th>
                <th scope="col">Name</th>
                <th scope="col">E-mail</th>
                <th scope="col">Admin</th>
                <th scope="col">Active</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="user" items="${users}">
                <tr id="user_${user.getId()}" onclick="goByUrl('/users/edit/${user.getId()}');">
                    <th scope="row">
                            ${user.getId()}
                    </th>
                    <td>
                            ${user.getName()}
                    </td>
                    <td>
                            ${user.getEmail()}
                    </td>
                    <td>
                        <c:if test="${user.getIsAdmin() == true}">
                            <em class="fa fa-check fa-l me-2"></em>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${user.getIsActive() == true}">
                            <em class="fa fa-check fa-l me-2"></em>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>
</body>
</html>