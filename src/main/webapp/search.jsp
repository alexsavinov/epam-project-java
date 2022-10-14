<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Search</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-5 h3 _main-color1">
        Search
    </div>

    <form method="POST" action="/search" id="searchForm">
        <div class="_search-form m-3 mt-2 p-3 border border-1 rounded _main-bg-color4 _main-color2 shadow-lg">

            <div class="d-flex row ">

                <%-- column 1 --%>
                <div class="col-12 col-md-4">

                    <div class="form-group mb-1">
                        <label for="daterange" class="form-label">
                            Date range
                        </label>
                        <input type="text" class="form-control _input-daterange"
                               id="daterange" name="daterange" aria-label="daterange"
                               value="${sessionScope.daterange}" required>
                    </div>

                    <div class="form-check mt-4 mb-3 pt-3">
                        <label class="form-check-label" for="showFullyReserved">
                            Show fully reserved routes
                        </label>
                        <input class="form-check-input" type="checkbox" value="" id="showFullyReserved"
                               <c:if test="${sessionScope.showFullyReserved}">checked</c:if> >
                    </div>

                </div>

                <%-- column 2 --%>

                <div class="col-12 col-md-4">
                    <div class="form-group mb-1">
                        <label for="station_departure_id" class="form-label fw-bold">
                            Station departure
                        </label>
                        <select class="form-control _input-station"
                                id="station_departure_id" name="station_departure_id">
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.getId()}"
                                        <c:if test="${station.getId() eq sessionScope.station_departure_id}">
                                            selected
                                        </c:if>>
                                        ${station.getName()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group mb-1">
                        <label for="station_arrival_id" class="form-label fw-bold">
                            Station arrival
                        </label>
                        <select class="form-control _input-station"
                                id="station_arrival_id" name="station_arrival_id">
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.getId()}"
                                        <c:if test="${station.getId() eq sessionScope.station_arrival_id}">
                                            selected
                                        </c:if>>
                                        ${station.getName()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <%-- column 3 --%>
                <div class="col-12 col-md-4">
                    <div class="d-flex flex-column">
                        <div class="mb-1 form-group d-flex">
                            <div>
                                <div>
                                    <label class="form-label">Travel cost from:</label>
                                </div>
                                <div class="d-flex g-0 m-0 p-0">
                                    <input type="number" class="form-control _input-number"
                                           id="cost_min" name="cost_min" aria-label="cost_min"
                                           data-bs-toggle="popover"
                                           data-bs-trigger="manual"
                                           data-bs-placement="bottom"
                                           data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}'
                                           data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999999"
                                           value="${sessionScope.cost_min}">
                                    <label for="cost_max" class="form-label ms-2 me-2 mt-1">
                                        to:
                                    </label>
                                    <input type="number" class="form-control _input-number"
                                           id="cost_max" name="cost_max" aria-label="cost_max"
                                           data-bs-placement="bottom" data-bs-title=" "
                                           data-container="body" data-bs-delay='{"show":100,"hide":300}'
                                           data-bs-toggle="tooltip" data-bs-trigger="manual"
                                           onchange="validate(this)" min="0" max="999999"
                                           value="${sessionScope.cost_max}">
                                </div>
                            </div>
                        </div>

                        <div class="mb-1 form-group d-flex">
                            <div>
                                <div>
                                    <label class="form-label">Travel time (in hours) from:</label>
                                </div>
                                <div class="d-flex g-0 m-0 p-0">
                                    <input type="number" class="form-control _input-number"
                                           id="travel_time_min" name="travel_time_min" aria-label="travel_time_min"
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.travel_time_min}">
                                    <label for="travel_time_max" class="form-label ms-2 me-2 mt-1">
                                        to:
                                    </label>
                                    <input type="number" class="form-control _input-number"
                                           id="travel_time_max" name="travel_time_max" aria-label="travel_time_max"
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.travel_time_max}">
                                </div>
                            </div>
                        </div>

                    </div>

                </div>
            </div>

            <div class="form-group mt-4">
                <button type="submit" class="btn btn-outline-secondary _btn_form">
                    <em class="fa fa-search fa-2l me-2"></em>
                    Search
                </button>
                <button type="reset" class="btn btn-outline-secondary _btn_form" onclick="resetForm()">
                    <em class="fa fa-refresh fa-2l me-2"></em>
                    Reset
                </button>
            </div>

        </div>
    </form>

    <div id="routes">
        <c:if test="${not empty routes and routes.size() ge 0}">
            <% request.setAttribute("orders", request.getSession().getAttribute("routes"));%>
            <% request.setAttribute("orders_tittle", "Routes found:");%>

            <div class="mt-0">
                <%@ include file="/parts/routes.jspf" %>
            </div>
        </c:if>
    </div>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>