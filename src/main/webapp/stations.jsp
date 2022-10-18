<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Stations" scope="page"/>

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

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1" href="/stations/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            Add ${url}
        </a>

        <table class="table table-striped table-hover p-5" aria-describedby="Stations">
            <thead>
            <tr>
                <th scope="col">id</th>
                <th scope="col">Name</th>
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