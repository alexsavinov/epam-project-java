<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Search</title>

    <script type="text/javascript">
        // const form = document.getElementById("searchForm");
        //
        // function handleForm(event) {
        //     event.preventDefault();
        // }
        //
        // form.addEventListener('submit', handleForm);
    </script>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-5 h3 _main-color1">
        Search
    </div>

    <div class="_search-form m-5 mt-2 border border-1 rounded _main-bg-color4 _main-color2 p-3 shadow-lg">
        <form method="POST" action="/search" id="searchForm">

            <div class="two-columns d-flex gap-3">

                <div class="col1 w-50">

                    <div class="form-group mb-1">
                        <label for="daterange" class="form-label">Date range</label>
                        <input type="text" class="form-control _input-daterange" aria-label="daterange"
                               id="daterange" name="daterange" value="${sessionScope.daterange}">
                    </div>

                    <div class="form-group mb-1">
                        <label for="station_departure_id" class="form-label">Station departure</label>
                        <select class="form-control _input-station" id="station_departure_id"
                                name="station_departure_id">
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.getId()}"
                                        <c:if test="${station.getId() eq sessionScope.station_departure_id}">selected</c:if>>
                                        ${station.getName()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group mb-1">
                        <label for="station_arrival_id" class="form-label">Station arrival</label>
                        <select class="form-control _input-station" id="station_arrival_id" name="station_arrival_id">
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.getId()}"
                                        <c:if test="${station.getId() eq sessionScope.station_arrival_id}">selected</c:if>>
                                        ${station.getName()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="col2 w-50 d-flex flex-column">
                    <div class="mb-1 form-group d-flex">
                        <div>
                            <div>
                                <label class="form-label">Travel cost from:</label>
                            </div>
                            <div class="d-flex g-0 m-0 p-0">
                                <input type="number" class="form-control _input-number" aria-label="cost_min"
                                       id="cost_min"
                                       name="cost_min" min="0" value="${sessionScope.cost_min}">
                                <label for="cost_max" class="form-label ms-2 me-2 mt-1">to: </label>
                                <input type="number" class="form-control _input-number" aria-label="cost_max"
                                       id="cost_max"
                                       name="cost_max" min="0" value="${sessionScope.cost_max}">
                            </div>
                        </div>
                    </div>

                    <div class="mb-1 form-group d-flex">
                        <div>
                            <div>
                                <label class="form-label">Travel time (in hours) from:</label>
                            </div>
                            <div class="d-flex g-0 m-0 p-0">
                                <input type="number" class="form-control _input-number" aria-label="travel_time_min"
                                       id="travel_time_min" name="travel_time_min" min="0"
                                       value="${sessionScope.travel_time_min}">
                                <label for="travel_time_max" class="form-label ms-2 me-2 mt-1">to: </label>
                                <input type="number" class="form-control _input-number" aria-label="travel_time_max"
                                       id="travel_time_max" name="travel_time_max" min="0"
                                       value="${sessionScope.travel_time_max}">
                            </div>
                        </div>
                    </div>


                    <div class="form-group mb-1">
                        <label for="min_seats" class="form-label">Minimum seats available</label>
                        <input type="number" class="form-control _input-number" aria-label="min_seats" id="min_seats"
                               name="min_seats" min="0" value="${sessionScope.min_seats}">
                    </div>

                    <%--                    <div class="form-check mt-4 mb-0">--%>
                    <%--                        <label class="form-check-label" for="searchCurrent">--%>
                    <%--                            Search only in current user's routes--%>
                    <%--                        </label>--%>
                    <%--                        <input class="form-check-input" type="checkbox" value="" id="searchCurrent"--%>
                    <%--                               <c:if test="${sessionScope.searchCurrent}">checked</c:if>>--%>
                    <%--                    </div>--%>

                    <%--                    <div class="form-check mt-4 mb-1">--%>
                    <%--                        <label class="form-check-label" for="showFullyReserved">--%>
                    <%--                            Show fully reserved routes--%>
                    <%--                        </label>--%>
                    <%--                        <input class="form-check-input" type="checkbox" value="" id="showFullyReserved"--%>
                    <%--                               <c:if test="${sessionScope.showFullyReserved}">checked</c:if> >--%>
                    <%--                    </div>--%>

                </div>

            </div>

            <div class="form-group mt-3">
                <button type="submit" class="btn">Search</button>
                <button type="reset" class="btn" onclick="resetForm()">Reset</button>
            </div>

        </form>
    </div>
    <%--    <div>sessionScope station_departure_id ${sessionScope.station_departure_id}</div>--%>

    <div id="routes">
        <c:if test="${not empty routes and routes.size() ge 0}">
            <% request.setAttribute("orders", request.getSession().getAttribute("routes"));%>
            <% request.setAttribute("orders_tittle", "Routes found:");%>

            <div class="mt-0">
                <%@ include file="/parts/routes.jspf" %>
            </div>

            <%--        <% request.getSession().removeAttribute("routes");%>--%>
        </c:if>
    </div>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>