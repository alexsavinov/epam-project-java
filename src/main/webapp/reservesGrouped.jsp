<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Your Reserves by Routes" scope="page"/>

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

        <table class="table table-striped table-hover mt-4"
               aria-describedby="Reserves">

            <thead class="_main-color4 _main-bg-color2 ">
            <tr class="">
                <th scope="col">Route</th>
                <th scope="col">Total seats reserved</th>
                <th scope="col">Ticket price</th>
                <th scope="col">Total travel cost</th>
            </tr>
            </thead>

            <tbody>
            <c:set var="total_travel_cost" value="0" scope="page"/>
            <c:set var="total_seats" value="0" scope="page"/>

            <c:forEach var="order" items="${reserves}">
                <%--                <c:set var="total_travel_cost" value="${total_travel_cost + order.getRoute().getTravelCost() * order.getSeats()}}" scope="page"/>--%>
                <c:set var="total_travel_cost"
                       value="${total_travel_cost + order.getRoute().getTravelCost() * order.getSeats()}" scope="page"/>
                <c:set var="total_seats" value="${total_seats + order.getSeats()}" scope="page"/>

                <tr id="selectBox_${order.getRoute().getId()}"
                    onclick="goByUrl('/reserves/edit/${order.getRoute().getId()}');">

                        <%--                    <th scope="row">--%>
                        <%--                            ${order.getId()}--%>
                        <%--                    </th>--%>
                    <td>
                            ${order.getRoute().getName()}
                    </td>
                        <%--                    <td>--%>
                        <%--                            ${order.getRoute().getSeatsAvailable()}--%>
                        <%--                    </td>--%>
                    <td>
                            ${order.getSeats()}
                    </td>
                    <td>
                            ${order.getRoute().getTravelCost()} $
                    </td>
                    <td>
                            ${order.getRoute().getTravelCost() * order.getSeats()} $
                    </td>
                        <%--                    <td>--%>
                        <%--                            ${order.getDateReserve()}--%>
                        <%--                    </td>--%>
                        <%--                    <td>--%>
                        <%--                        <button type="button"--%>
                        <%--                                class="btn btn-outline-secondary _btn_form"--%>
                        <%--                                data-bs-toggle="modal"--%>
                        <%--                                data-bs-target="#reserveDeleteModal"--%>
                        <%--                                onclick='setValue("order_id", ${order.getId()});--%>
                        <%--                                        setValue("seats", ${order.getSeats()});--%>
                        <%--                                        setAttributeValue("seats", "max", ${order.getSeats()});'>--%>
                        <%--                            Delete--%>
                        <%--                        </button>--%>
                        <%--                    </td>--%>
                </tr>
            </c:forEach>
            </tbody>

            <tfoot>
            <tr class="fw-bold">
                <td>Total:</td>
                <td>${total_seats}</td>
                <td></td>
                <td>${total_travel_cost} $</td>
            </tr>
            </tfoot>
        </table>
    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/reserveDeleteModal.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>