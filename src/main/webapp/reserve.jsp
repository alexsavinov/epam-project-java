<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="page_title" value="Your Orders for Route:" scope="page"/>

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
    <title>Railway ticket office - ${page_title}</title>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <div id="reserve" class="d-flex flex-column align-items-center mb-4">

        <div class="m-3 mt-4">

            <%-- train_number --%>
            <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline _main-color4 _main-bg-color2 rounded-4 rounded-bottom">
                <div class="h3">
                    Train number:
                </div>
                <div class="fs-3">
                    ${route.getTrainNumber()}
                </div>
            </div>

            <%-- station_departure --%>
            <div class="mb-2 p-3 d-flex gap-1 justify-content-between">
                <div>
                    <div class="h3">
                        From:
                    </div>
                    <div class="">
                        <div class="h2 d-flex justify-content-start _main-color4">
                            ${route.getStationDeparture().getName()}
                        </div>
                        <div class="fs-5">
                            departure at ${route.getDateDeparture()}
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
                        To:
                    </div>
                    <div class="">
                        <div class="h2 d-flex justify-content-start _main-color4">
                            ${route.getStationArrival().getName()}
                        </div>
                        <div class="fs-5">
                            arrival at ${route.getDateArrival()}
                        </div>
                    </div>
                </div>

            </div>


            <div class="d-flex justify-content-between _main-bg-color2 rounded-4 rounded-top">
                <%-- seats_total --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-dark _main-color3">Total seats: ${route.getSeatsTotal()}</span>
<%--                    <div class="fs-3">--%>
<%--                        Total seats:--%>
<%--                    </div>--%>
<%--                    <div class="fs-3">--%>
<%--                        ${route.getSeatsTotal()}--%>
<%--                    </div>--%>
                </div>

                <%-- seats_reserve --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-warning _main-color1">Seats reserved: ${route.getSeatsReserved()}</span>
<%--                    <div class="fs-3">--%>
<%--                        Seats reserved:--%>
<%--                    </div>--%>
<%--                    <div class="fs-3">--%>
<%--                        ${route.getSeatsReserved()}--%>
<%--                    </div>--%>
                </div>

                <%-- seats_available --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge bg-success _main-color3">Seats available: ${route.getSeatsAvailable()}</span>
<%--                    <div class="fs-3">--%>
<%--                        Seats available:--%>
<%--                    </div>--%>
<%--                    <div class="fs-3">--%>
<%--                        ${route.getSeatsAvailable()}--%>
<%--                    </div>--%>
                </div>

                <%-- seats_available --%>
                <div class="mb-2 p-3 d-flex justify-content-between align-items-baseline">
                    <span class="badge _main-color3 _main-bg-color4">Your seats: ${total_seats}</span>
<%--                    <div class="h3 _main-color4">--%>
<%--                        Your seats:--%>
<%--                    </div>--%>
<%--                    <div class="fs-3 _main-color4 fw-bold">--%>
<%--                        ${total_seats}--%>
<%--                    </div>--%>
                </div>
            </div>


        </div>

    </div>

    <c:if test="${reserves ne null and not empty reserves and reserves.size() ge 0}">
        <hr>

        <div id="reserves">
            <div class="mt-4">
                    <%--                    <c:if test="${paginator ne null}">--%>
                    <%--                    <div class="h3 ms-5 _main-color1">--%>
                    <%--                        Reserves found: ${reserves.size()}--%>
                    <%--                    </div>--%>
                    <%--                    </c:if>--%>

                    <%--                    <% request.setAttribute("tickets", request.getSession().getAttribute("routes"));%>--%>
                <%@ include file="/parts/reserves.jspf" %>

            </div>
        </div>
    </c:if>

    <%--    <c:if test="${tickets ne null and not empty tickets and tickets.size() ge 0}">--%>
    <%--        <div id="routes">--%>
    <%--            <div class="mt-4">--%>
    <%--                    &lt;%&ndash;                    <c:if test="${paginator ne null}">&ndash;%&gt;--%>
    <%--                <div class="h3 ms-5 _main-color1">--%>
    <%--                    tickets found: ${tickets.size()}--%>
    <%--                </div>--%>
    <%--                    &lt;%&ndash;                    </c:if>&ndash;%&gt;--%>

    <%--                    &lt;%&ndash;                    <% request.setAttribute("tickets", request.getSession().getAttribute("routes"));%>&ndash;%&gt;--%>
    <%--                <%@ include file="/parts/tickets.jspf" %>--%>

    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </c:if>--%>

    <%@ include file="/parts/message.jspf" %>
</div>


<%@ include file="/parts/footer.jspf" %>
<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>