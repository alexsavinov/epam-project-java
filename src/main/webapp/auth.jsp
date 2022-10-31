<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<c:set var="key_title" value="auth_jsp.title" scope="page"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>

    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script>
        function onSubmit(token) {
            document.getElementById("demo-form").submit();
        }
    </script>

    <script>
        function successCaptcha() {
            document.getElementById("submit_button").disabled = false;
        }
    </script>

    <title>
        <fmt:message key='index_jsp.title'/> - <fmt:message key='${key_title}'/>
    </title>

</head>
<body>

<div class="_wrapper">

    <%@ include file="/parts/header.jspf" %>
    <div class="ms-5 mt-5 h3 _main-color1 d-flex justify-content-center">
        <fmt:message key='auth_jsp.header'/>
    </div>

    <div class="d-flex justify-content-center">

        <form class="ms-5 mt-4 p-3 border border-1 rounded _form _main-bg-color4 _main-color2 shadow-lg"
              method="POST" action="/login" id="loginForm">

            <div class="mb-1">
                <label for="name" class="form-label">
                    <fmt:message key='auth_jsp.username'/>
                </label>
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder=
                    <fmt:message key='auth_jsp.username'/>
                            aria-label="name" id="name" name="name"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                    <button type="button" class="btn btn-sm btn-warn" data-bs-container="body"
                            onfocus="showPopover(this)"
                            data-bs-toggle="popover" data-bs-placement="right"
                            data-bs-trigger="focus"
                            data-bs-html="true"
                            data-bs-content="
                        <ul>
                            <li><fmt:message key='users.user.help_1'/></li>
                            <li><fmt:message key='users.user.help_2'/></li>
                            <li><fmt:message key='users.user.help_3'/></li>
                            <li><fmt:message key='users.user.help_4'/></li>
                            <li><fmt:message key='users.user.help_5'/></li>
                            <li>Character only is allowed</li>
                        </ul>"
                            title=<fmt:message key='users.user.help_tittle'/>>
                        ?
                    </button>
                </div>

            </div>

            <div class="mb-1">
                <label for="password" class="form-label">
                    <fmt:message key='users.password'/>
                </label>
                <div class="input-group mb-3">
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder=
                           <fmt:message key='users.password'/> aria-describedby="passwordHelpBlock"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                    <button type="button" class="btn btn-sm btn-warn" data-bs-container="body"
                            data-bs-toggle="popover" data-bs-placement="right"
                            data-bs-trigger="focus" data-bs-html="true"
                            onfocus="showPopover(this)"
                            data-bs-content="
                        <ul>
                            <li><fmt:message key='users.password.help_1'/></li>
                            <li><fmt:message key='users.password.help_2'/></li>
                            <li><fmt:message key='users.password.help_3'/></li>
                            <li><fmt:message key='users.password.help_4'/></li>
                            <li><fmt:message key='users.password.help_5'/></li>
                        </ul>"
                            title=
                            <fmt:message key='users.password.help_tittle'/>>
                        ?
                    </button>
                </div>
            </div>

            <div class="form-group">

                <form action="?" method="POST">
                    <div class="g-recaptcha" data-sitekey="6LfXXKwiAAAAAHo-hVUm0ap9z-hjBFzCA7wgOA5o"
                         data-callback="successCaptcha"></div>
                    <button type="submit" id="submit_button" disabled
                            class="btn mt-3 _btn_form <customTags:access role="authorized" modifier="disabled"/>">
                        <fmt:message key='auth_jsp.btn.login'/>
                    </button>
                </form>

                <a id="register_button" onclick="goByUrl('/register')"
                   class="btn mt-3 _btn_form <customTags:access role="authorized" modifier="disabled"/>">
                    <fmt:message key='auth_jsp.btn.register'/>
                </a>
            </div>

        </form>

    </div>


    <%--    <button class="g-recaptcha"--%>
    <%--            data-sitekey="6LfOWqwiAAAAAECWjWR8VWQWyy094CT6o6b2z58v"--%>
    <%--            data-callback='onSubmit'--%>
    <%--            data-action='submit'>Submit</button>--%>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>