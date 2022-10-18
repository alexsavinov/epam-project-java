<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Routes" scope="page"/>

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
    <%@ include file="/parts/message.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1" href="/routes/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            Add ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-3" aria-describedby="Routes">
            <thead>
            <tr>
                <th scope="col">id</th>
                <th scope="col">Train #</th>
                <th scope="col">Departure</th>
                <th scope="col">Date/time</th>
                <th scope="col">Arrival</th>
                <th scope="col">Date/time</th>
                <th scope="col">Travel time</th>
                <th scope="col">Travel cost</th>
                <th scope="col">Seats total</th>
                <th scope="col">Seats available</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="route" items="${routes}">
                <tr id="selectBox_${route.getId()}" onclick="goByUrl('/routes/edit/${route.getId()}');">
                    <th scope="row">
                            ${route.getId()}
                    </th>
                    <td>
                            ${route.getTrainNumber()}
                    </td>
                    <td>
                            ${route.getStationDeparture().getName()}
                    </td>
                    <td>
                            ${route.getDateDeparture()}
                    </td>
                    <td>
                            ${route.getTravelTime()}
                    </td>
                    <td>
                            ${route.getStationArrival().getName()}
                    </td>
                    <td>
                            ${route.getDateArrival()}
                    </td>
                    <td>
                            ${route.getTravelCost()}
                    </td>
                    <td>
                            ${route.getSeatsTotal()}
                    </td>
                    <td>
                            ${route.getSeatsAvailable()}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>