<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>

    <title>Railway ticket office - User route</title>
</head>
<body onload="updateTime();">

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%@ include file="/parts/body_top.jspf" %>


<%--    <div id="data"></div>--%>


    <div class="ms-5 h3 _main-color1">
        User route
        <c:choose>
            <c:when test="${action == 'add'}">
                (add)
            </c:when>
            <c:when test="${action == 'edit'}">
                (edit)
            </c:when>
            <c:otherwise>
                (view)
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty messages}">
        <div class="alert alert-light m-5 shadow alert-dismissible fade show" role="alert">
            <i class="fa fa-info-circle fa-2xl me-2"></i>
                ${messages}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>

        <c:set var="messages" value="${null}" scope="session"/>
    </c:if>

    <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color3 p-3 shadow-lg me-5"
          method="POST">

        <div class="form-group mb-1">
            <label for="id" class="form-label">id</label>
            <input type="text" class="form-control" aria-label="id" id="id" name="id" disabled
                   value="${userRoute.getId()}">
        </div>

        <div class="form-group mb-1">
            <label for="seats" class="form-label">Seats reserved</label>
            <input type="number" class="form-control" aria-label="seats"
                   id="seats" name="seats" value="${userRoute.getSeats()}">
        </div>

        <div class="form-group mb-1">
            <label for="date_reserve" class="form-label">Date of reserve</label>
            <input type="text" class="form-control" aria-label="date_reserve"
                   id="date_reserve" name="date_reserve" value="${userRoute.getDate_reserve()}">
        </div>

        <div class="form-group mb-1">
            <label for="user">User</label>
            <select class="form-control" id="user" name="user" onchange="updateUser();">
                <c:forEach var="user" items="${users}">
                    <option value="${user.getId()}"
                            <c:if test="${user.getId() eq userRoute.getUser().getId()}">selected</c:if>>
                            ${user.getName()}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group mb-1">
            <label for="route">Route</label>
            <select class="form-control" id="route" name="route" onchange="updateRoute();">
                <c:forEach var="route" items="${routes}">
                    <option value="${route.getId()}"
                            <c:if test="${route.getId() eq userRoute.getRoute().getId()}">selected</c:if>>
                            ${route.getName()}
                    </option>
                </c:forEach>
            </select>
        </div>

        <hr>

        <div class="form-group border _main-color2 rounded mt-3 mb-3 p-2">
            <div class="h5">User details</div>

            <div class="mb-1">
                <label for="train_number" class="form-label">id</label>
                <input type="text" class="form-control" placeholder="user_id" aria-label="user_id"
                       id="user_id" name="user_id" value="${userRoute.getUser().getId()}" disabled>
            </div>

            <div class="mb-1">
                <label for="user_name" class="form-label">name</label>
                <input type="text" class="form-control" placeholder="user_name" aria-label="user_name"
                       id="user_name" name="user_name" value="${userRoute.getUser().getName()}" disabled>
            </div>
        </div>

        <hr>

        <div class="form-group border _main-color2 rounded mt-3 mb-3 p-2">
            <div class="h5">Route details</div>

            <div class="mb-1">
                <label for="train_number" class="form-label">Train number</label>
                <input type="text" class="form-control" placeholder="Train number" aria-label="train_number"
                       id="train_number" name="train_number" value="${userRoute.getRoute().getTrain_number()}" disabled>
            </div>

            <div class="form-group mb-1">
                <label for="station_departure">Station departure</label>
                <input type="text" class="form-control" aria-label="station_departure"
                       id="station_departure" name="station_departure" value="${userRoute.getRoute().getStation_departure().getName()}" disabled>
            </div>

            <div class="mb-1">
                <label for="date_departure" class="form-label">Date departure</label>
                <input type="datetime-local" class="form-control" aria-label="date_departure" onchange="updateTime();"
                       id="date_departure" name="date_departure" value="${userRoute.getRoute().getDate_departure()}"
                       max="${userRoute.getRoute().getDate_arrival()}" disabled>
            </div>

            <div class="form-group mb-1">
                <label for="station_arrival">Station arrival</label>
                <input type="text" class="form-control" aria-label="station_arrival"
                       id="station_arrival" name="station_arrival" value="${userRoute.getRoute().getStation_arrival().getName()}" disabled>
            </div>

            <div class="mb-1">
                <label for="date_arrival" class="form-label">Date arrival</label>
                <input type="datetime-local" class="form-control" aria-label="date_arrival" onchange="updateTime();"
                       id="date_arrival" name="date_arrival" value="${userRoute.getRoute().getDate_arrival()}"
                       min="${userRoute.getRoute().getDate_departure()}" disabled>
            </div>

            <div class="mb-1">
                <label for="travel_time" class="form-label">Travel time</label>
                <input type="text" class="form-control" aria-label="travel_time"
                       id="travel_time" name="travel_time" disabled>
            </div>

            <div class="mb-1">
                <label for="travel_cost" class="form-label">Travel cost</label>
                <input type="number" class="form-control" aria-label="travel_cost"
                       id="travel_cost" name="travel_cost" value="${userRoute.getRoute().getTravel_cost()}" disabled>
            </div>

            <div class="mb-1">
                <label for="seats_available" class="form-label">Seats available</label>
                <input type="number" class="form-control" aria-label="seats_available"
                       id="seats_available" name="seats_available" value="${userRoute.getRoute().getSeats_available()}"
                       disabled>
            </div>

            <div class="mb-1">
                <label for="seats_total" class="form-label">Seats total</label>
                <input type="number" class="form-control" aria-label="seats_total"
                       id="seats_total" name="seats_total" value="${userRoute.getRoute().getSeats_total()}" disabled>
            </div>
        </div>


        <div class="form-group mt-3">
            <button type="submit" class="btn">Save</button>
            <a class="btn btn-outline-secondary" href="/users-routes/delete/${userRoute.getId()}">Delete</a>
        </div>
    </form>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>