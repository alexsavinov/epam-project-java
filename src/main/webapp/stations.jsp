<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="stations.stations" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title><fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/></title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-3 <customTags:access role="user" modifier="d-none"/>"
           href="/stations/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            <fmt:message key='actions.add'/> ${url}
        </a>

        <table class="table table-striped table-hover p-5" aria-describedby="Stations">
            <thead class="table-secondary">
            <tr>
                <th scope="col">id</th>
                <th scope="col"><fmt:message key='stations.name'/></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="station" items="${stations}">
                <tr id="selectBox_${station.getId()}"
                    onclick="goByUrl('/stations/edit/${station.getId()}');">
                    <th scope="row">
                            ${station.getId()}
                    </th>
                    <td>
                            ${station.getName()}
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