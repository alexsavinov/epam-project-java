<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title><fmt:message key='index_jsp.title'/> - <fmt:message key='search_jsp.search'/></title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-3 h3 _main-color4">
        <fmt:message key='search_jsp.search'/>
    </div>

    <%-- align-items-start align-items-sm-center --%>
    <form method="POST" action="/search" id="searchForm" onsubmit="return validateForm()">
        <div class="_search-form d-flex flex-column m-3 ms-lg-5 me-lg-5 mt-2 p-3 border border-1 rounded _main-bg-color4 _main-color2 shadow-lg">

            <div class="d-flex flex-column align-items-start flex-md-row">
                <div class="d-flex flex-column flex-lg-row justify-content-between">

                    <%-- Daterange, Train number  --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Daterange --%>
                        <div class="form-group me-lg-3 mb-1 ms-1">
                            <label for="daterange" class="form-label">
                                <fmt:message key='search_jsp.date_range'/>:
                            </label>
                            <input type="text" class="form-control _input-daterange"
                                   id="daterange" name="daterange" aria-label="daterange"
                                   value="${sessionScope.daterange}" required>
                        </div>

                        <%-- Train number --%>
                        <div class="form-group me-lg-3 mb-1 ms-1">
                            <label for="train_number" class="form-label">
                                <fmt:message key='routes.train_number'/>:
                            </label>
                            <input type="text" class="form-control _input-train-number"
                                   id="train_number" name="train_number" aria-label="train_number"
                                   value="${sessionScope.train_number}">
                        </div>
                    </div>

                    <%-- Stations --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Station departure --%>
                        <div class="form-group ms-1 me-1 mb-1">
                            <label for="station_departure_id" class="form-label">
                                <fmt:message key='routes.station_departure'/>:
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
                        <div class="form-group ms-1 me-1 mb-1">
                            <label for="station_arrival_id" class="form-label">
                                <fmt:message key='routes.station_arrival'/>:
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
                    <%-- Travel cost, Travel time --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Travel cost --%>
                        <div class="d-flex flex-column me-md-1 ms-1 mb-1">
                            <div>
                                <label class="form-label">
                                    <fmt:message key='search_jsp.travel_cost_from'/>:
                                </label>
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
                                    <fmt:message key='words.to'/>:
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
                        <div class="form-group d-flex flex-column ms-1 mb-1">

                            <div>
                                <label class="form-label">
                                    <fmt:message key='search_jsp.travel_time_from'/>:
                                </label>
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
                                    <fmt:message key='words.to'/>:
                                </label>
                                <div class="input-group _input-number ">
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

                    <%-- Seats available, Seats reserved --%>
                    <div class="d-flex flex-column border rounded p-1 me-md-3 mb-3">

                        <%-- Seats available --%>
                        <div class="mb-1 form-group d-flex flex-column mt-0 ms-1">

                            <div>
                                <label class="form-label">
                                    <fmt:message key='search_jsp.seats_available_from'/>:
                                </label>
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
                                    <fmt:message key='words.to'/>:
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

                        <%-- Seats reserved --%>
                        <div class="mb-1 form-group d-flex flex-column mt-0 ms-1">

                            <div>
                                <label class="form-label">
                                    <fmt:message key='search_jsp.seats_reserved_from'/>:
                                </label>
                            </div>
                            <div class="d-flex g-0 m-0 p-0">

                                <%-- seats_reserved_min --%>
                                <div class="input-group _input-number">
                                    <input type="number" class="form-control"
                                           id="seats_reserved_min" name="seats_reserved_min"
                                           aria-label="seats_reserved_min"
                                           data-bs-toggle="popover" data-bs-trigger="manual"
                                           data-bs-placement="bottom" data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.seats_reserved_min}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('seats_reserved_min')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>

                                <%-- seats_reserved_max --%>
                                <label for="seats_available_max" class="form-label ms-2 me-2 mt-1">
                                    <fmt:message key='words.to'/>:
                                </label>
                                <div class="input-group _input-number">
                                    <input type="number" class="form-control"
                                           id="seats_reserved_max" name="seats_reserved_max"
                                           aria-label="seats_reserved_max"
                                           data-bs-toggle="popover" data-bs-trigger="manual"
                                           data-bs-placement="bottom" data-ds-container="body"
                                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                                           onchange="validate(this)" min="0" max="999"
                                           value="${sessionScope.seats_reserved_max}">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            onclick="setEmpty('seats_reserved_max')" type="button">
                                        <em class="fa fa-xmark fa-2l"></em>
                                    </button>
                                </div>

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
                    <fmt:message key='words.search'/>
                </button>
                <%-- Reset --%>
                <button id="reset_button" type="reset" class="btn btn-outline-secondary _btn_form"
                        onclick="resetSearchForm()">
                    <em class="fa fa-refresh fa-2l me-2"></em>
                    <fmt:message key='actions.reset'/>
                </button>
            </div>

            <%-- Sorting --%>
            <input type="hidden" id="sort_train_number" name="sort_train_number"
                   value="${sessionScope.sort_train_number}"/>

            <input type="hidden" id="sort_station_departure" name="sort_station_departure"
                   value="${sessionScope.sort_station_departure}"/>

            <input type="hidden" id="sort_date_departure" name="sort_date_departure"
                   value="${sessionScope.sort_date_departure}"/>

            <input type="hidden" id="sort_travel_time" name="sort_travel_time"
                   value="${sessionScope.sort_travel_time}"/>

            <input type="hidden" id="sort_station_arrival" name="sort_station_arrival"
                   value="${sessionScope.sort_station_arrival}"/>

            <input type="hidden" id="sort_date_arrival" name="sort_date_arrival"
                   value="${sessionScope.sort_date_arrival}"/>

            <input type="hidden" id="sort_travel_cost" name="sort_travel_cost"
                   value="${sessionScope.sort_travel_cost}"/>

            <input type="hidden" id="sort_seats_reserved" name="sort_seats_reserved"
                   value="${sessionScope.sort_seats_reserved}"/>

            <input type="hidden" id="sort_seats_available" name="sort_seats_available"
                   value="${sessionScope.sort_seats_available}"/>

            <input type="hidden" id="sort_seats_total" name="sort_seats_total"
                   value="${sessionScope.sort_seats_total}"/>

            <%-- Pagination --%>
            <input type="hidden" id="pg_page" name="pg_page" value="0"/>

        </div>
    </form>

    <div id="routes">
        <div class="mt-4">
            <c:if test="${paginator ne null}">
                <div class="h3 ms-5 _main-color1">
                    <fmt:message key='tittles.routes_found'/>: ${paginator.getResults()}
                </div>
            </c:if>
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