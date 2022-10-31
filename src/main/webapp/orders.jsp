<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="orders.orders" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/>
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
    <%@ include file="/parts/bodyTop.jspf" %>

    <div class="_wrapper-table">

        <a class="btn btn-sm btn-outline-secondary mt-2 mb-3 shadow-sm <customTags:access role="user" modifier="d-none"/>"
           href="/orders/add">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
            <fmt:message key='actions.add'/> ${url}
        </a>

        <table class="table table-striped table-hover p-5 mt-1" aria-describedby="Orders">
            <thead class="table-secondary">
            <tr>
                <th scope="col">id</th>
                <th scope="col"><fmt:message key='routes.seats_reserved'/></th>
                <th scope="col"><fmt:message key='orders.date_of_reserve'/></th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;"><fmt:message key='users.user'/> (id)</th>
                <th scope="col" class="bg-light" style="border: 1px solid lightgrey;"><fmt:message key='users.user'/> (<fmt:message key='users.name'/>)</th>
                <th scope="col"><fmt:message key='routes.route'/> (id)</th>
                <th scope="col"><fmt:message key='routes.train_number_column'/></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr id="selectBox_${order.getId()}"
                    onclick="goByUrl('/orders/edit/${order.getId()}');">

                    <th scope="row">
                            ${order.getId()}
                    </th>
                    <td>
                            ${order.getSeats()}
                    </td>
                    <td>
                            ${order.getDateReserve()}
                    </td>
                    <td>
                            ${order.getUser().getId()}
                    </td>
                    <td>
                            ${order.getUser().getName()}
                    </td>
                    <td>
                            ${order.getRoute().getId()}
                    </td>
                    <td>
                            ${order.getRoute().getTrainNumber()}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>