<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


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
            <button type="submit" class="btn mt-3 <customTags:access role="authorized" modifier="disabled"/>">
                <fmt:message key='auth_jsp.btn.login'/>
            </button>
        </div>

    </form>

    <%@ include file="/parts/message.jspf" %>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/bodyBottom.jspf" %>


</body>
</html>