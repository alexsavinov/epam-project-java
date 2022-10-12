<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Stations</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/body_top.jspf" %>

    <div class="ms-5 h3 _main-color1">
        Stations
    </div>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1" href="/stations/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            Add ${url}
        </a>

        <table class="table table-striped table-hover p-5">
            <thead>
            <tr>
                <th scope="col">id</th>
                <th scope="col">Name</th>
                <%--                <th scope="col">Action</th>--%>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="station" items="${stations}">

                <tr id="selectBox_${station.getId()}" onclick="goByUrl('/stations/edit/${station.getId()}');">
                        <th scope="row">
<%--                            <a href="/stations/edit/${station.getId()}">--%>
                                    ${station.getId()}
<%--                            </a>--%>
                        </th>
                        <td>
<%--                            <a href="/stations/edit/${station.getId()}">--%>
                                    ${station.getName()}
<%--                            </a>--%>
                        </td>
                            <%--                    <td>--%>
                            <%--                        <a class="btn btn-sm btn-outline-secondary"--%>
                            <%--                           href="/stations/delete/${station.getId()}">Delete</a>--%>
                            <%--                    </td>--%>
                </tr>
                    </a>

            </c:forEach>

            </tbody>
        </table>
    </div>
    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>