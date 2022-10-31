<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='profile_jsp.title'/></title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="ms-5 mt-3 h3 _main-color4">
        <fmt:message key='profile_jsp.title'/>
    </div>

    <div class="_wrapper-profile">

        <div class="_wrapper-profile-btn">

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm <customTags:access role="user" modifier="d-none"/>"
               href="/users">
                <em class="fa fa-user-circle fa-3x _profile-btn"></em>
                <fmt:message key='users.users'/>
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm"
               href="/stations">
                <em class="fa fa-university fa-3x _profile-btn"></em>
                <fmt:message key='stations.stations'/>
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm <customTags:access role="user" modifier="d-none"/>"
               href="/routes">
                <em class="fa fa-map fa-3x _profile-btn"></em>
                <fmt:message key='routes.routes'/>
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm <customTags:access role="user" modifier="d-none"/>"
               href="/orders">
                <em class="fa fa-window-restore fa-3x _profile-btn"></em>
                <fmt:message key='profile_jsp.orders'/>
            </a>

            <a class="btn btn-outline-secondary _btn_profile mb-2 shadow-sm"
               href="/reserves/grouped">
                <em class="fa fa-handshake fa-3x _profile-btn"></em>
                <fmt:message key='profile_jsp.reserves'/>
            </a>

        </div>
    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>