<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Users routes</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="ms-5 h3 _main-color1">
        Users routes
    </div>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1 shadow-sm" href="/users-routes/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            Add ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-3">
            <caption>Users routes</caption>
            <thead>
            <tr>
                <th scope="col">id</th>
                <th scope="col">Seats reserved</th>
                <th scope="col">Date of reserve</th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;">user id</th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;">user name</th>
                <th scope="col">route id</th>
                <th scope="col">Train #</th>
                <th scope="col">Departure</th>
                <th scope="col">Date/time</th>
                <th scope="col">Arrival</th>
                <th scope="col">Date/time</th>
                <th scope="col">Travel time</th>
                <th scope="col">Travel cost</th>
                <th scope="col">Seats total</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="order" items="${userRoutes}">
                <tr id="selectBox_${order.getId()}"
                    onclick="goByUrl('/users-routes/edit/${order.getId()}');">

                    <th scope="row">
                            ${order.getId()}
                    </th>
                    <td>
                            ${order.getSeats()}
                    </td>
                    <td>
                            ${order.getDate_reserve()}
                    </td>
                    <td>
                            ${order.getUser().getId()}
                    </td>
                    <td>
                            ${order.getUser().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getId()}
                    </td>
                    <td>
                            ${order.getRoute().getTrain_number()}
                    </td>
                    <td>
                            ${order.getRoute().getStation_departure().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getDate_departure()}
                    </td>
                    <td>
                            ${order.getRoute().getStation_arrival().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getDate_arrival()}
                    </td>
                    <td>
                            ${order.getRoute().getTravel_time()}
                    </td>
                    <td>
                            ${order.getRoute().getTravel_cost()}
                    </td>
                    <td>
                            ${order.getRoute().getSeats_total()}
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