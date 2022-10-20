<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Search</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-3 h3 _main-color4">
        Search
    </div>
    <%--    align-items-start align-items-sm-center--%>
    <form method="POST" action="/search" id="searchForm" onsubmit="return validateForm()">
        <div class="_search-form d-flex flex-column m-3 ms-lg-5 me-lg-5 mt-2 p-3 border border-1 rounded _main-bg-color4 _main-color2 shadow-lg">

            <div class="d-flex flex-column align-items-start flex-md-row">
                <div class="d-flex flex-column flex-lg-row justify-content-between">

                    <%-- Daterange, Train number  --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Daterange --%>
                        <div class="form-group me-lg-3 mb-1 ms-1">
                            <label for="daterange" class="form-label">
                                Date range
                            </label>
                            <input type="text" class="form-control _input-daterange"
                                   id="daterange" name="daterange" aria-label="daterange"
                                   value="${sessionScope.daterange}" required>
                        </div>

                        <%-- Train number --%>
                        <div class="form-group me-lg-3 mb-1 ms-1">
                            <label for="train_number" class="form-label">
                                Train number
                            </label>
                            <input type="text" class="form-control _input-train-number"
                                   id="train_number" name="train_number" aria-label="train_number"
                                   value="${sessionScope.train_number}">
                        </div>
                    </div>

                    <%-- Stations --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Station departure --%>
                        <div class="form-group mb-1">
                            <label for="station_departure_id" class="form-label">
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

                        <%-- Station arrival --%>
                        <div class="form-group mb-1">
                            <label for="station_arrival_id" class="form-label">
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
                </div>

                <div class="d-flex flex-column flex-xxl-row justify-content-between">
                    <div class="d-flex flex-column p-1 me-md-3 mb-2">

                        <%-- Travel cost --%>
                        <div class="form-group d-flex flex-column mb-1">
                            <div>
                                <label class="form-label">Travel cost from:</label>
                            </div>
                            <div class="d-flex g-0 m-0 p-0">
                                <%-- cost_min--%>
                                <div class="input-group _input-number">
                                    <input type="number" class="form-control"
                                           id="cost_min" name="cost_min" aria-label="cost_min"
                                           data-bs-toggle="popover" data-bs-trigger="manual"
                                           data-bs-placement="bottom" data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999999"
                                           value="${sessionScope.cost_min}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('cost_min')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>

                                <%-- cost_max--%>
                                <label for="cost_max" class="form-label ms-2 me-2 mt-1">
                                    to:
                                </label>
                                <div class="input-group _input-number">
                                    <input type="number" class="form-control"
                                           id="cost_max" name="cost_max" aria-label="cost_max"
                                           data-bs-placement="bottom" data-bs-title=" "
                                           data-container="body" data-bs-delay='{"show":100,"hide":300}'
                                           data-bs-toggle="tooltip" data-bs-trigger="manual"
                                           onchange="validate(this)" min="0" max="999999"
                                           value="${sessionScope.cost_max}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('cost_max')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>
                            </div>

                        </div>

                        <%-- Travel time --%>
                        <div class="form-group d-flex flex-column mb-3 mb-lg-0">

                            <div>
                                <label class="form-label">Travel time (in hours) from:</label>
                            </div>
                            <div class="d-flex g-0 m-0 p-0">
                                <%-- travel_time_min --%>
                                <div class="input-group _input-number">
                                    <input type="number" class="form-control"
                                           id="travel_time_min" name="travel_time_min"
                                           aria-label="travel_time_min"
                                           data-bs-toggle="popover" data-bs-trigger="manual"
                                           data-bs-placement="bottom" data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.travel_time_min}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('travel_time_min')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>

                                <%-- travel_time_max --%>
                                <label for="travel_time_max" class="form-label ms-2 me-2 mt-1">
                                    to:
                                </label>
                                <div class="input-group  _input-number">
                                    <input type="number" class="form-control"
                                           id="travel_time_max" name="travel_time_max"
                                           aria-label="travel_time_max"
                                           data-bs-toggle="popover" data-bs-trigger="manual"
                                           data-bs-placement="bottom" data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.travel_time_max}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('travel_time_max')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>
                            </div>

                        </div>

                    </div>

                    <%-- Seats available --%>
                    <div class="mb-1 form-group d-flex flex-column mt-1 ms-1">

                        <div>
                            <label class="form-label">Seats available from:</label>
                        </div>
                        <div class="d-flex g-0 m-0 p-0">
                            <%-- seats_available_min --%>
                            <div class="input-group _input-number">
                                <input type="number" class="form-control"
                                       id="seats_available_min" name="seats_available_min"
                                       aria-label="seats_available_min"
                                       data-bs-toggle="popover" data-bs-trigger="manual"
                                       data-bs-placement="bottom" data-ds-container="body"
                                       data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                       onchange="validate(this)" min="0" max="999"
                                       value="${sessionScope.seats_available_min}">
                                <button class="btn btn-sm btn-outline-secondary"
                                        onclick="setEmpty('seats_available_min')" type="button">
                                    <em class="fa fa-xmark fa-2l"></em>
                                </button>
                            </div>

                            <%-- seats_available_max --%>
                            <label for="seats_available_max" class="form-label ms-2 me-2 mt-1">
                                to:
                            </label>
                            <div class="input-group _input-number">
                                <input type="number" class="form-control"
                                       id="seats_available_max" name="seats_available_max"
                                       aria-label="seats_available_max"
                                       data-bs-toggle="popover" data-bs-trigger="manual"
                                       data-bs-placement="bottom" data-ds-container="body"
                                       data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                       onchange="validate(this)" min="0" max="999"
                                       value="${sessionScope.seats_available_max}">
                                <button class="btn btn-sm btn-outline-secondary"
                                        onclick="setEmpty('seats_available_max')" type="button">
                                    <em class="fa fa-xmark fa-2l"></em>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <%-- BUTTONS --%>
            <div class="form-group mt-3">
                <%-- Search --%>
                <button id="submit_button" type="submit" class="btn btn-outline-secondary _btn_form">
                    <em class="fa fa-search fa-2l me-2"></em>
                    Search
                </button>
                <%-- Reset --%>
                <button id="reset_button" type="reset" class="btn btn-outline-secondary _btn_form"
                        onclick="resetSearchForm()">
                    <em class="fa fa-refresh fa-2l me-2"></em>
                    Reset
                </button>
            </div>

            <%-- Sorting --%>
            <input type="hidden" id="sort_train_number" name="sort_train_number"
                   value="${sessionScope.sort_train_number}"/>

            <%-- Pagination --%>
            <input type="hidden" id="pg_page" name="pg_page" value="0"/>

        </div>
    </form>

    <div id="routes">
        <div class="mt-4">
            <div class="h3 ms-5 _main-color1">
                Routes found: ${paginator.getResults()}
            </div>
            <c:if test="${routes ne null and not empty routes and routes.size() ge 0}">
                <% request.setAttribute("routes", request.getSession().getAttribute("routes"));%>
                <%@ include file="/parts/routes.jspf" %>
            </c:if>
        </div>
    </div>

    <%@ include file="/parts/message.jspf" %>

<%--    <%@ include file="/parts/footer.jspf" %>--%>

</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>