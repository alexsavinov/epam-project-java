<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags"%>

<%--<mylib:add x="2" y="3" op="-"/>--%>
<%--<hr>--%>
<%--<mylib:add x="2" y="0" op="/"/>--%>

<%@ taglib prefix="mylib2" uri="http://com.my/mylib2" %>

<mylib2:op x="2" y="3" op="-"/>

${ex}

<hr>

<mylib2:hello name="Obama"/>



<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='auth_jsp.title'/>
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>
            <div class="ms-5 mt-5 h3 _main-color1">
                <fmt:message key='auth_jsp.header'/>
            </div>

            <form class="ms-5 mt-4 p-3 border border-1 rounded _form _main-bg-color4 _main-color2 shadow-lg"
                  method="POST" action="/login">

                <div class="mb-1">
                    <label for="name" class="form-label">
                        <fmt:message key='auth_jsp.username'/>
                    </label>
                    <input type="text" class="form-control" placeholder="Username"
                           aria-label="name" id="name" name="name">
                </div>

                <div class="mb-1">
                    <label for="password" class="form-label">
                        <fmt:message key='auth_jsp.password'/>
                    </label>
                    <input type="password" class="form-control" id="password" name="password">
                </div>

                <div class="form-group">
                    <button type="submit" class="btn mt-3">
                        <fmt:message key='auth_jsp.btn.login'/>
                    </button>
                </div>

            </form>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/body_bottom.jspf" %>


</body>
</html>