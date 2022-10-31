<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="tittles.route_information" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/> (${action})
    </title>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>
    <%@ include file="/parts/message.jspf" %>

    <%-- Input form AND piechart  --%>
    <div class="d-md-flex justify-content-evenly">

        <%-- FORM --%>
        <div class="ms-5 mt-4 d-flex flex-column  align-items-end">
            <%--        <form rounded _form _main-bg-color4 _main-color3 p-3 shadow-lg me-5 w-50"--%>
            <%--            method="POST">--%>

            <%-- id --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="id" class="form-label me-3 fw-bold">id</label>
                <input type="text" class="form-control _input-station" aria-label="id"
                       id="id" name="id" disabled
                       value="${route.getId()}">
            </div>

            <%-- train_number --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="train_number" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.train_number'/>
                </label>
                <input type="text" class="form-control _input-station"
                       placeholder="<fmt:message key='routes.train_number'/>"
                       aria-label="train_number"
                       id="train_number" name="train_number"
                       value="${route.getTrainNumber()}" disabled>
            </div>

            <%-- station_departure --%>
            <div class="form-group mb-1 d-flex align-items-end">
                <label for="station_departure" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.station_departure'/>
                </label>
                <input type="text" class="form-control _input-station"
                       placeholder="<fmt:message key='routes.station_departure'/>"
                       aria-label="station_departure"
                       id="station_departure" name="station_departure"
                       value="${route.getStationDeparture().getName()}" disabled>
            </div>

            <%-- date_departure --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="date_departure" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.date_time_departure'/>
                </label>
                <input type="datetime-local" class="form-control _input-station" aria-label="date_departure"
                       id="date_departure" name="date_departure"
                       onchange="updateTime();"
                       value="${route.getDateDeparture()}"
                       max="${route.getDateArrival()}"
                       disabled>
            </div>

            <%-- station_arrival --%>
            <div class="form-group mb-1 d-flex align-items-end">
                <label for="station_arrival" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.station_arrival'/>
                </label>
                <input type="text" class="form-control _input-station"
                       placeholder="<fmt:message key='routes.station_arrival'/>"
                       aria-label="station_arrival"
                       id="station_arrival" name="station_arrival"
                       value="${route.getStationArrival().getName()}"
                       disabled>
            </div>

            <%-- date_arrival --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="date_arrival" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.date_time_arrival'/>
                </label>
                <input type="datetime-local" class="form-control _input-station" aria-label="date_arrival"
                       id="date_arrival" name="date_arrival"
                       onchange="updateTime();"
                       value="${route.getDateArrival()}"
                       min="${route.getDateDeparture()}"
                       disabled>
            </div>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_time" class="form-label me-3 fw-bold">Travel time</label>--%>
            <%--            <div>11</div>--%>
            <%--            <input type="text" class="form-control" aria-label="travel_time"--%>
            <%--                   id="travel_time" name="travel_time" value="${route.getTravelTime()}" disabled>--%>
            <%--        </div>--%>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_cost" class="form-label me-3 fw-bold"><fmt:message key='words.TravelCost'/></label>--%>
            <%--            <input type="number" class="form-control" aria-label="travel_cost"--%>
            <%--                   id="travel_cost" name="travel_cost" value="${route.getTravelCost()}">--%>
            <%--        </div>--%>

            <%-- seats_reserve --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="seats_reserved" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.seats_reserved'/>
                </label>
                <input type="number" class="form-control _input-station" aria-label="seats_reserved"
                       id="seats_reserved" name="seats_reserved"
                       value="${route.getSeatsReserved()}" disabled>
            </div>

            <%-- seats_available --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="seats_available" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.seats_available'/>
                </label>
                <input type="number" class="form-control _input-station" aria-label="seats_available"
                       id="seats_available" name="seats_available"
                       value="${route.getSeatsAvailable()}" disabled>
            </div>

            <%-- seats_total --%>
            <div class="mb-1 d-flex align-items-end">
                <label for="seats_total" class="form-label me-3 fw-bold">
                    <fmt:message key='routes.seats_total'/>
                </label>
                <input type="number" class="form-control _input-station" aria-label="seats_total"
                       id="seats_total" name="seats_total"
                       value="${route.getSeatsTotal()}" disabled>
            </div>

        </div>

        <div>

            <div id="piechart" class="ms-5 mt-5"></div>
            <div id="chart_div" class="ms-5 mt-5"></div>
        </div>

    </div>

    <c:if test="${not empty orders and orders.size() ge 0}">
    <div class="m-1 mt-5">
        <% request.setAttribute("orders", request.getAttribute("ordersByRoute"));%>
        <% request.setAttribute("orders_tittle", "tittles.orders_by_route");%>
        <%@ include file="/parts/orders.jspf" %>
    </div>
    </c:if>

    <%@ include file="/parts/footer.jspf" %>
    <%@ include file="/parts/bodyBottom.jspf" %>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChartPie);

        function drawChartPie() {
            const seats_available = document.getElementById('seats_available').value;
            const seats_reserved = document.getElementById('seats_reserved').value;

            const data = google.visualization.arrayToDataTable([
                ['Title', 'Value'],
                ['Available seats: ' + seats_available, seats_available / 100],
                ['Reserved seats: ' + seats_reserved, seats_reserved / 100]
            ]);

            const options = {
                title: ((currentLocale == 'uk') ? 'Місця маршрута' : 'Seats of route'),
                is3D: true,
            };

            const chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
        }

        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        let dataTickets = ((currentLocale == 'uk') ? [['День', 'Квитки']] : [['Day', 'Tickets']]);

        let parent = document.querySelector('#order_container');
        if (parent) {

            for (let node of parent.childNodes) {
                if (node.childNodes[1]) {
                    const dateChart = node.childNodes[7].innerText.trim().substring(0, 10);
                    const valueChart = parseInt(node.childNodes[5].innerText);
                    console.log(dateChart)
                    console.log(valueChart)
                    dataTickets.push([dateChart, valueChart]);
                }
            }
        }

        function drawChart() {
            let data = google.visualization.arrayToDataTable(dataTickets);
            let options = {
                title: ((currentLocale == 'uk') ? 'Продажі квитків за період' : 'Tickets sales by period'),
                hAxis: {title: 'Year', titleTextStyle: {color: '#333'}},
                vAxis: {minValue: 0}
            };
            if (dataTickets.length > 1) {
                var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
        }
    </script>

</body>
</html>