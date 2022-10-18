<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Your reserves" scope="page"/>

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

        <%--        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1" href="/reserves/add">--%>
        <%--            <i class="fa fa-plus-circle" aria-hidden="true"></i>--%>
        <%--            Add ${url}--%>
        <%--        </a>--%>

        <table class="table table-striped table-hover p-5" aria-describedby="Reserves">

            <thead>
            <tr>
                <th scope="col">id</th>
                <th scope="col">Route</th>
                <th scope="col">Seats available</th>
                <th scope="col">Reserve</th>
                <th scope="col">Date of reserve</th>
                <th scope="col">Action</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="order" items="${reserves}">
                <tr id="selectBox_${order.getId()}"
                    onclick="goByUrl('/reserves/edit/${order.getId()}');">
                    <th scope="row">
                            ${order.getId()}
                    </th>
                    <td>
                            ${order.getRoute().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getSeatsAvailable()}
                    </td>
                    <td>
                            ${order.getSeats()}
                    </td>
                    <td>
                            ${order.getDateReserve()}
                    </td>
                    <td>
<%--                        <button type="button"--%>
<%--                                class="btn btn-outline-secondary _btn_form"--%>
<%--                                data-bs-toggle="modal"--%>
<%--                                data-bs-target="#exampleModal"--%>
<%--                                onclick='setValue("route_id", ${order.getId()})'>--%>
<%--                            Reserve--%>
<%--                        </button>--%>
                        <a class="btn btn-outline-secondary" href="/reserves/delete/${order.getId()}">
                            Delete
                        </a>
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