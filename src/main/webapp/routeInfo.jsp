<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>


    <head>
        <title><fmt:message key='index_jsp.title'/> - Route information</title>
    </head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>
    <%@ include file="/parts/message.jspf" %>

    <div class="ms-5 h3 _main-color1">
        Route information
    </div>

    <%-- Input form AND piechart  --%>
    <div class="d-md-flex">

        <%-- FORM --%>
        <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color3 p-3 shadow-lg me-5 w-50"
              method="POST">

            <%-- id --%>
            <div class="mb-1">
                <label for="id" class="form-label">id</label>
                <input type="text" class="form-control" aria-label="id"
                       id="id" name="id" disabled
                       value="${route.getId()}">
            </div>

            <%-- train_number --%>
            <div class="mb-1">
                <label for="train_number" class="form-label">Train number</label>
                <input type="text" class="form-control" placeholder="Train number" aria-label="train_number"
                       id="train_number" name="train_number"
                       value="${route.getTrainNumber()}" disabled>
            </div>

            <%-- station_departure --%>
            <div class="form-group mb-1">
                <label for="station_departure"><fmt:message key='words.stationDeparture'/></label>
                <input type="text" class="form-control" placeholder="<fmt:message key='words.stationDeparture'/>" aria-label="station_departure"
                       id="station_departure" name="station_departure"
                       value="${route.getStationDeparture().getName()}" disabled>
            </div>

            <%-- date_departure --%>
            <div class="mb-1">
                <label for="date_departure" class="form-label">Date departure</label>
                <input type="datetime-local" class="form-control" aria-label="date_departure"
                       id="date_departure" name="date_departure"
                       onchange="updateTime();"
                       value="${route.getDateDeparture()}"
                       max="${route.getDateArrival()}"
                       disabled>
            </div>

            <%-- station_arrival --%>
            <div class="form-group mb-1">
                <label for="station_arrival"><fmt:message key='words.stationArrival'/></label>
                <input type="text" class="form-control" placeholder="<fmt:message key='words.stationArrival'/>" aria-label="station_arrival"
                       id="station_arrival" name="station_arrival"
                       value="${route.getStationArrival().getName()}"
                       disabled>
            </div>

            <%-- date_arrival --%>
            <div class="mb-1">
                <label for="date_arrival" class="form-label">Date arrival</label>
                <input type="datetime-local" class="form-control" aria-label="date_arrival"
                       id="date_arrival" name="date_arrival"
                       onchange="updateTime();"
                       value="${route.getDateArrival()}"
                       min="${route.getDateDeparture()}"
                       disabled>
            </div>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_time" class="form-label">Travel time</label>--%>
            <%--            <div>11</div>--%>
            <%--            <input type="text" class="form-control" aria-label="travel_time"--%>
            <%--                   id="travel_time" name="travel_time" value="${route.getTravelTime()}" disabled>--%>
            <%--        </div>--%>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_cost" class="form-label"><fmt:message key='words.TravelCost'/></label>--%>
            <%--            <input type="number" class="form-control" aria-label="travel_cost"--%>
            <%--                   id="travel_cost" name="travel_cost" value="${route.getTravelCost()}">--%>
            <%--        </div>--%>

            <%-- seats_reserve --%>
            <div class="mb-1">
                <label for="seats_reserved" class="form-label">Seats reserved</label>
                <input type="number" class="form-control" aria-label="seats_reserved"
                       id="seats_reserved" name="seats_reserved"
                       value="${route.getSeatsReserved()}" disabled>
            </div>

            <%-- seats_available --%>
            <div class="mb-1">
                <label for="seats_available" class="form-label"><fmt:message key='words.SeatsAvailable'/></label>
                <input type="number" class="form-control" aria-label="seats_available"
                       id="seats_available" name="seats_available"
                       value="${route.getSeatsAvailable()}" disabled>
            </div>

            <%-- seats_total --%>
            <div class="mb-1">
                <label for="seats_total" class="form-label"><fmt:message key='words.SeatsTotal'/></label>
                <input type="number" class="form-control" aria-label="seats_total"
                       id="seats_total" name="seats_total"
                       value="${route.getSeatsTotal()}" disabled>
            </div>

            <%-- BUTTONS --%>
            <%--            <div class="form-group mt-3">--%>
            <%--                <button type="submit" class="btn">--%>
            <%--                    Confirm--%>
            <%--                </button>--%>
            <%--                <a class="btn btn-outline-secondary" href="/search">--%>
            <%--                    Cancel--%>
            <%--                </a>--%>
            <%--            </div>--%>

        </form>

        <%-- piechart --%>
        <%--        <div id="piechart" class="ms-5 mt-5" style="width: 400px; height: 500px;"></div>--%>
        <div>

            <div id="piechart" class="ms-5 mt-5"></div>
            <div id="chart_div" class="ms-5 mt-5"></div>
        </div>

        <%--    <div>--%>

        <%--        <%@ include file="/parts/orders.jspf" %>--%>

        <%--    </div>--%>

        <%-- Table of orders --%>
        <%--    <div id="orders_wrapper" class="mt-5 <customTags:access role="guest" modifier="d-none"/>">--%>

    </div>

    <c:if test="${not empty orders and orders.size() ge 0}">
    <div class="m-1 mt-5">
        <% request.setAttribute("orders", request.getAttribute("ordersByRoute"));%>
        <% request.setAttribute("orders_tittle", "Orders by route");%>
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
                title: 'Seats by route',
                is3D: true,
            };

            const chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
        }

        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        let dataTickets = [['Day', 'Tickets']];

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
                title: 'Tickets sales by period',
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