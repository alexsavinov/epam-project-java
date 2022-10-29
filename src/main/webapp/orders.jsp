<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='profile_jsp.orders'/>
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1 shadow-sm <customTags:access role="user" modifier="d-none"/>"
           href="/orders/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            <fmt:message key='words.add'/> ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-3" aria-describedby="Orders">
            <thead class="table-secondary">
            <tr>
                <th scope="col">id</th>
                <th scope="col">Seats reserved</th>
                <th scope="col">Date of reserve</th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;">user id</th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;">user name</th>
                <th scope="col">route id</th>
                <th scope="col"><fmt:message key='words.trainNumberColumn'/></th>
                <th scope="col">Departure</th>
                <th scope="col">Date/time</th>
                <th scope="col">Arrival</th>
                <th scope="col">Date/time</th>
                <th scope="col"><fmt:message key='words.TravelTime'/></th>
                <th scope="col"><fmt:message key='words.TravelCost'/></th>
                <th scope="col">Seats reserved</th>
                <th scope="col"><fmt:message key='words.SeatsAvailable'/></th>
                <th scope="col"><fmt:message key='words.SeatsTotal'/></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr id="selectBox_${order.getId()}"
                    onclick="goByUrl('/orders/edit/${order.getId()}');">

                    <th scope="row">
                            ${order.getId()}
                    </th>
                    <td>
                            ${order.getSeats()}
                    </td>
                    <td>
                            ${order.getDateReserve()}
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
                            ${order.getRoute().getTrainNumber()}
                    </td>
                    <td>
                            ${order.getRoute().getStationDeparture().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getDateDeparture()}
                    </td>
                    <td>
                            ${order.getRoute().getStationArrival().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getDateArrival()}
                    </td>
                    <td>
                            ${order.getRoute().getTravelTime()}
                    </td>
                    <td>
                            ${order.getRoute().getTravelCost()}
                    </td>
                    <td>
                            ${order.getRoute().getSeatsReserved()}
                    </td>
                    <td>
                            ${order.getRoute().getSeatsAvailable()}
                    </td>
                    <td>
                            ${order.getRoute().getSeatsTotal()}
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