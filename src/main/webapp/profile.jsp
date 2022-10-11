<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Profile</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-5 h3 _main-color1">
        Current features:
    </div>

    <div class="d-flex flex-column ms-5 mt-3 w-25">
        <a class="btn btn-outline-secondary mb-2 shadow-sm" href="/users">Users</a>
        <a class="btn btn-outline-secondary mb-2 shadow-sm" href="/stations">Stations</a>
        <a class="btn btn-outline-secondary mb-2 shadow-sm" href="/routes">Routes</a>
        <a class="btn btn-outline-secondary mb-2 shadow-sm" href="/users-routes">Users routes</a>
    </div>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>