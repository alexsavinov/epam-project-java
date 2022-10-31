<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="routes.route" scope="page"/>

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

    <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color3 p-3 shadow-lg me-5"
          method="POST">

        <div class="mb-1">
            <label for="id" class="form-label">id</label>
            <input type="text" class="form-control" aria-label="id" id="id" name="id" disabled
                   value="${route.getId()}">
        </div>

        <div class="mb-1">
            <label for="train_number" class="form-label">
                <fmt:message key='routes.train_number'/>
            </label>
            <input type="text" class="form-control"
                   id="train_number" name="train_number"
                   minlength="2"
                   value="${route.getTrainNumber()}"
                   aria-label="train_number"
                   placeholder=
            <fmt:message key='routes.train_number'/>>
        </div>

        <div class="form-group mb-1">
            <label for="station_departure">
                <fmt:message key='routes.station_departure'/>
            </label>
            <select class="form-control" id="station_departure" name="station_departure">
                <c:forEach var="station" items="${stations}">
                    <option value="${station.getId()}"
                            <c:if test="${station.getId() eq route.getStationDeparture().getId()}">selected</c:if>>
                            ${station.getName()}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-1">
            <label for="date_departure" class="form-label">
                <fmt:message key='routes.date_time_departure'/>
            </label>
            <input type="datetime-local" class="form-control" aria-label="date_departure"
                   onchange="updateTime();"
                   id="date_departure" name="date_departure"
                   value="${route.getDateDeparture()}"
                   max="${route.getTravelTime()}">
        </div>

        <div class="form-group mb-1">
            <label for="station_arrival">
                <fmt:message key='routes.station_arrival'/>
            </label>
            <select class="form-control" id="station_arrival" name="station_arrival">
                <c:forEach var="station" items="${stations}">
                    <option value="${station.getId()}"
                            <c:if test="${station.getId() eq route.getStationArrival().getId()}">selected</c:if>>
                            ${station.getName()}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-1">
            <label for="date_arrival" class="form-label">
                <fmt:message key='routes.date_time_arrival'/>
            </label>
            <input type="datetime-local" class="form-control" aria-label="date_arrival" onchange="updateTime();"
                   id="date_arrival" name="date_arrival"
                   value="${route.getDateArrival()}"
                   min="${route.getDateDeparture()}">
        </div>

        <%--        <div class="mb-1">--%>
        <%--            <label for="travel_time" class="form-label">Travel time</label>--%>
        <%--            <div>11</div>--%>
        <%--            <input type="text" class="form-control" aria-label="travel_time"--%>
        <%--                   id="travel_time" name="travel_time" value="${route.getTravelTime()}" disabled>--%>
        <%--        </div>--%>

        <div class="mb-1">
            <label for="travel_cost" class="form-label">
                <fmt:message key='routes.travel_cost'/>
            </label>
            <input type="number" class="form-control" aria-label="travel_cost"
                   id="travel_cost" name="travel_cost"
                   min="0" max="9999999"
                   value="${route.getTravelCost()}"
                   placeholder="<fmt:message key='routes.travel_cost'/>">
        </div>

        <div class="mb-1">
            <label for="seats_reserved" class="form-label">
                <fmt:message key='routes.seats_reserved'/>
            </label>
            <input type="number" class="form-control" aria-label="seats_reserved"
                   id="seats_reserved" name="seats_reserved"
                   min="0" max="9999999"
                   value="${route.getSeatsReserved()}"
                   placeholder="<fmt:message key='routes.seats_reserved'/>">
        </div>

        <div class="mb-1">
            <label for="seats_total" class="form-label">
                <fmt:message key='routes.seats_total'/>
            </label>
            <input type="number" class="form-control" aria-label="seats_total"
                   id="seats_total" name="seats_total"
                   min="0" max="9999999"
                   value="${route.getSeatsTotal()}"
                   placeholder="<fmt:message key='routes.seats_total'/>">
        </div>

        <div class=" form-group mt-3">
            <button type="submit" class="btn" <customTags:access role="user" modifier="disabled"/>>
                <fmt:message key='actions.save'/>
            </button>
            <a class="btn btn-outline-secondary <customTags:access role="user" modifier="disabled"/>"
               href="/routes/delete/${route.getId()}">
                <fmt:message key='actions.delete'/>
            </a>
        </div>

    </form>

    <%--    <%@ include file="/parts/ordersjspf">--%>
    <%--        <jsp:param name="orders" value="${userRoutes}"/>--%>
    <%--    </jsp:include>--%>

    <%--    <c:if test="${not empty ordersByRoute and ordersByRoute.size() ge 0}">--%>
    <%--        <% request.setAttribute("orders", request.getAttribute("ordersByRoute"));%>--%>
    <%--        <% request.setAttribute("orders_tittle", "Orders by route");%>--%>

    <%--        <div class="mt-5">--%>
    <%--            <%@ include file="/parts/orders.jspf" %>--%>
    <%--        </div>--%>
    <%--    </c:if>--%>

    <%--    <c:if test="${not empty ordersByCurrentUser and ordersByCurrentUser.size() ge 0}">--%>
    <%--        <% request.setAttribute("orders", request.getAttribute("ordersByCurrentUser"));%>--%>
    <%--        <% request.setAttribute("orders_tittle", "Orders by logged user"); %>--%>

    <%--        <div class="mt-5">--%>
    <%--            <%@ include file="/parts/orders.jspf" %>--%>
    <%--        </div>--%>
    <%--    </c:if>--%>

    <%--    <c:if test="${not empty orders and orders.size() ge 0}">--%>
    <%--        <% request.setAttribute("orders", request.getAttribute("orders"));%>--%>
    <%--        <% request.setAttribute("orders_tittle", "Orders all");%>--%>

    <%--        <div class="mt-5">--%>
    <%--            <%@ include file="/parts/orders.jspf" %>--%>
    <%--        </div>--%>
    <%--    </c:if>--%>

    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>