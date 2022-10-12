<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>


    <head>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {

                const seats_total = document.getElementById('seats_total').value;
                const seats_available = document.getElementById('seats_available').value;
                // console.log(document.getElementById('seats_total').value)
                // console.log(document.getElementById('seats_available').value)

                const data = google.visualization.arrayToDataTable([
                    ['Title', 'Value'],
                    ['Available seats', seats_total / 100],
                    ['Reserved seats', (seats_total - seats_available) / 100]
                    // ['Reserved seats', document.getElementById('seats_total').value - document.getElementById('seats_available').value],
                    // ['Available seats', document.getElementById('seats_available').value],
                ]);

                const options = {
                    title: 'Total seats by routes',
                    is3D: true,
                };

                const chart = new google.visualization.PieChart(document.getElementById('piechart'));

                chart.draw(data, options);
            }
        </script>

        <title>Railway ticket office - Reserve</title>
    </head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/body_top.jspf" %>

    <%@ include file="/parts/message.jspf" %>

    <div class="ms-5 h3 _main-color1">
        Route reserve
    </div>

    <%-- Input form AND piechart  --%>
    <div class="d-md-flex">


        <%-- FORM --%>
        <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color3 p-3 shadow-lg me-5 w-50"
              method="POST">

            <%-- id --%>
            <div class="mb-1">
                <label for="id" class="form-label">id</label>
                <input type="text" class="form-control" aria-label="id" id="id" name="id" disabled
                       value="${route.getId()}">
            </div>

            <%-- train_number --%>
            <div class="mb-1">
                <label for="train_number" class="form-label">Train number</label>
                <input type="text" class="form-control" placeholder="Train number" aria-label="train_number"
                       id="train_number" name="train_number" value="${route.getTrain_number()}" disabled>
            </div>

            <%-- station_departure --%>
            <div class="form-group mb-1">
                <label for="station_departure">Station departure</label>
                <input type="text" class="form-control" placeholder="Station departure" aria-label="station_departure"
                       id="station_departure" name="station_departure"
                       value="${route.getStation_departure().getName()}" disabled>
            </div>

            <%-- date_departure --%>
            <div class="mb-1">
                <label for="date_departure" class="form-label">Date departure</label>
                <input type="datetime-local" class="form-control" aria-label="date_departure"
                       id="date_departure" name="date_departure"
                       onchange="updateTime();"
                       value="${route.getDate_departure()}" max="${route.getDate_arrival()}" disabled>
            </div>

            <%-- station_arrival --%>
            <div class="form-group mb-1">
                <label for="station_arrival">Station arrival</label>
                <input type="text" class="form-control" placeholder="Station arrival" aria-label="station_arrival"
                       id="station_arrival" name="station_arrival"
                       value="${route.getStation_arrival().getName()}" disabled>
            </div>

            <%-- date_arrival --%>
            <div class="mb-1">
                <label for="date_arrival" class="form-label">Date arrival</label>
                <input type="datetime-local" class="form-control" aria-label="date_arrival"
                       id="date_arrival" name="date_arrival"
                       onchange="updateTime();"
                       value="${route.getDate_arrival()}" min="${route.getDate_departure()}" disabled>
            </div>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_time" class="form-label">Travel time</label>--%>
            <%--            <div>11</div>--%>
            <%--            <input type="text" class="form-control" aria-label="travel_time"--%>
            <%--                   id="travel_time" name="travel_time" value="${route.getTravel_time()}" disabled>--%>
            <%--        </div>--%>

            <%--        <div class="mb-1">--%>
            <%--            <label for="travel_cost" class="form-label">Travel cost</label>--%>
            <%--            <input type="number" class="form-control" aria-label="travel_cost"--%>
            <%--                   id="travel_cost" name="travel_cost" value="${route.getTravel_cost()}">--%>
            <%--        </div>--%>

            <%-- seats_available --%>
            <div class="mb-1">
                <label for="seats_available" class="form-label">Seats available</label>
                <input type="number" class="form-control" aria-label="seats_available"
                       id="seats_available" name="seats_available"
                       value="${route.getSeats_available()}" disabled>
            </div>

            <%-- seats_total --%>
            <div class="mb-1">
                <label for="seats_total" class="form-label">Seats total</label>
                <input type="number" class="form-control" aria-label="seats_total"
                       id="seats_total" name="seats_total"
                       value="${route.getSeats_total()}" disabled>
            </div>

            <%-- seats_reserve --%>
            <div class="mb-1">
                <label for="seats_reserve" class="form-label">Seats to reserve</label>
                <input type="number" class="form-control" aria-label="seats_reserve"
                       id="seats_reserve" name="seats_reserve">
            </div>

            <%-- BUTTONS --%>
            <div class="form-group mt-3">
                <button type="submit" class="btn">Confirm</button>
                <a class="btn btn-outline-secondary" href="/search">Cancel</a>
            </div>

        </form>

        <%-- piechart --%>
<%--        <div id="piechart" class="ms-5 mt-5" style="width: 400px; height: 500px;"></div>--%>
        <div id="piechart" class="ms-5 mt-5 w-50"></div>

    </div>

    <%--    <%@ include file="/parts/users-routes.jsp">--%>
    <%--        <jsp:param name="orders" value="${userRoutes}"/>--%>
    <%--    </jsp:include>--%>

    <c:if test="${not empty userRoutesByRoute and userRoutesByRoute.size() ge 0}">
        <% request.setAttribute("orders", request.getAttribute("userRoutesByRoute"));%>
        <% request.setAttribute("orders_tittle", "Orders by route");%>

        <div class="mt-5">
            <%@ include file="/parts/users-routes.jsp"/>
        </div>
    </c:if>

    <c:if test="${not empty userRoutesByCurrentUser and userRoutesByCurrentUser.size() ge 0}">
        <% request.setAttribute("orders", request.getAttribute("userRoutesByCurrentUser"));%>
        <% request.setAttribute("orders_tittle", "Orders by logged user"); %>

        <div class="mt-5">
            <%@ include file="/parts/users-routes.jsp"/>
        </div>
    </c:if>

    <c:if test="${not empty userRoutes and userRoutes.size() ge 0}">
        <% request.setAttribute("orders", request.getAttribute("userRoutes"));%>
        <% request.setAttribute("orders_tittle", "Orders all");%>

        <div class="mt-5">
            <%@ include file="/parts/users-routes.jsp"/>
        </div>
    </c:if>


    <%@ include file="/parts/footer.jspf" %>


</div>


<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>