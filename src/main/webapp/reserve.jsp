<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="reserves.orders_tittle" scope="page"/>

<c:set var="total_travel_cost" value="0" scope="page"/>
<c:set var="total_seats" value="0" scope="page"/>
<c:if test="${reserves ne null and not empty reserves and reserves.size() ge 0}">
    <c:forEach var="order" items="${reserves}">
        <c:set var="total_travel_cost" scope="page"
               value="${total_travel_cost + route.getTravelCost() * order.getSeats()}"/>
        <c:set var="total_seats" scope="page"
               value="${total_seats + order.getSeats()}"/>
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title><fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/></title>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>
    <%@ include file="/parts/message.jspf" %>

    <div id="reserve" class="d-flex flex-column align-items-center mb-4">

        <div class="m-3 mt-4">

            <%-- train_number --%>
            <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline _main-color4 _main-bg-color2 rounded-4 rounded-bottom">
                <div class="h3">
                    <fmt:message key='routes.train_number'/>:
                </div>
                <div class="fs-3">
                    ${route.getTrainNumber()}
                </div>
            </div>

            <%-- station_departure --%>
            <div class="mb-2 p-3 d-flex gap-1 justify-content-between">
                <div>
                    <div class="h3">
                        <fmt:message key='tittles.from'/>:
                    </div>
                    <div class="">
                        <div class="h2 d-flex justify-content-start _main-color4">
                            ${route.getStationDeparture().getName()}
                        </div>
                        <div class="fs-5">
                            <fmt:message key='tittles.departure_at'/> ${route.getDateDeparture()}
                        </div>
                    </div>
                </div>

                <div class="d-flex align-items-center">
                    <div class="_dot"></div>
                    <div class="_line"></div>
                    <div class="_dot"></div>
                </div>

                <div>
                    <div class="h3">
                        <fmt:message key='tittles.to'/>:
                    </div>
                    <div class="">
                        <div class="h2 d-flex justify-content-start _main-color4">
                            ${route.getStationArrival().getName()}
                        </div>
                        <div class="fs-5">
                            <fmt:message key='tittles.arrival_at'/> ${route.getDateArrival()}
                        </div>
                    </div>
                </div>

            </div>


            <div class="d-flex justify-content-between _main-bg-color2 rounded-4 rounded-top">
                <%-- seats_total --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-dark _main-color3">
                        <fmt:message key='routes.seats_total'/>: ${route.getSeatsTotal()}
                    </span>
                </div>

                <%-- seats_reserve --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-warning _main-color1">
                        <fmt:message key='routes.seats_reserved'/>: ${route.getSeatsReserved()}
                    </span>
                </div>

                <%-- seats_available --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-success _main-color3">
                        <fmt:message key='routes.seats_available'/>: ${route.getSeatsAvailable()}
                    </span>
                </div>

                <%-- seats_available --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge _main-color3 _main-bg-color4">
                        <fmt:message key='routes.your_seats'/>: ${total_seats}
                    </span>
                </div>
            </div>

        </div>

    </div>

    <c:if test="${reserves ne null and not empty reserves and reserves.size() ge 0}">
        <hr>
        <div id="reserves">
            <div class="mt-4">
                <%@ include file="/parts/reserves.jspf" %>
            </div>
        </div>
    </c:if>

</div>

<%@ include file="/parts/footer.jspf" %>
<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>