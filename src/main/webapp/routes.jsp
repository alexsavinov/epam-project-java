<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='profile_jsp.routes'/>
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>
    <%@ include file="/parts/message.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-1 <customTags:access role="user" modifier="d-none"/>"
           href="/routes/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            <fmt:message key='words.add'/> ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-3" aria-describedby="Routes">
            <thead class="table-secondary">
            <tr>
                <th scope="col">id</th>
                <th scope="col"><fmt:message key='words.trainNumberColumn'/></th>
                <th scope="col"><fmt:message key='words.stationDeparture'/></th>
                <th scope="col"><fmt:message key='words.DateTimeDeparture'/></th>
                <th scope="col"><fmt:message key='words.stationArrival'/></th>
                <th scope="col"><fmt:message key='words.DateTimeArrival'/></th>
                <th scope="col"><fmt:message key='words.TravelTime'/></th>
                <th scope="col"><fmt:message key='words.TravelCost'/></th>
                <th scope="col"><fmt:message key='words.SeatsTotal'/></th>
                <th scope="col"><fmt:message key='words.SeatsAvailable'/></th>
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