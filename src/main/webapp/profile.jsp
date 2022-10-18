<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Profile</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-3 h3 _main-color4">
        Profile
    </div>

    <div class="_wrapper-profile">

        <div class="_wrapper-profile-btn">

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm <customTags:access role="user" modifier="d-none"/>"
               href="/users">
                <em class="fa fa-user-circle fa-3x _profile-btn"></em>
                Users
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm" href="/stations">
                <em class="fa fa-university fa-3x _profile-btn"></em>
                Stations
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm" href="/routes">
                <em class="fa fa-map fa-3x _profile-btn"></em>
                Routes
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm" href="/orders">
                <em class="fa fa-window-restore fa-3x _profile-btn"></em>
                Orders
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm" href="/reserves">
                <em class="fa fa-handshake fa-3x _profile-btn"></em>
                Reserves
            </a>

            <%--        <em class="fa fa-search fa-2xl me-2"></em>--%>


        </div>
    </div>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>