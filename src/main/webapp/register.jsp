<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

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
            document.getElementById("register_submit_button").disabled = false;
        }
    </script>

    <title>
        <%--        <fmt:message key='auth_jsp.title'/>--%>
        Registration
    </title>

</head>
<body>

<div class="_wrapper">

    <%@ include file="/parts/header.jspf" %>
    <div class="ms-5 mt-5 h3 _main-color1 d-flex justify-content-center">
        <%--        <fmt:message key='auth_jsp.header'/>--%>
        Registration
    </div>

    <div class="d-flex justify-content-center">

        <form class="ms-5 mt-4 p-3 border border-1 rounded _form _main-bg-color4 _main-color2 shadow-lg"
              method="POST" action="/register" id="registerForm">

            <%-- NAME --%>
            <div class="mb-1">
                <label for="name" class="form-label">
                    <fmt:message key='auth_jsp.username'/>
                </label>
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="Name"
                           aria-label="name" id="name" name="name"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                    <button type="button" class="btn btn-sm btn-warn" data-bs-container="body"
                            data-bs-toggle="popover" data-bs-placement="right"
                            data-bs-trigger="focus"
                            data-bs-html="true"
                            title="Your username must:"
                            data-bs-content="
                        <ul>
                            <li>Minimum 6 characters</li>
                            <li>Only numbers are not allowed at least one character should be there</li>
                            <li>contain at least 1 lowercase character (a-z)</li>
                            <li>No special characters allowed except</li>
                            <li>No space allowed</li>
                            <li>Character only is allowed</li>
                        </ul>"
                            onfocus="showPopover(this)">
                        ?
                    </button>
                </div>
            </div>

            <%-- EMAIL --%>
            <div class="mb-1">
                <label for="email" class="form-label">
                    <fmt:message key='auth_jsp.email'/>
                </label>
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="E-mail"
                           aria-label="email" id="email" name="email"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                    <button type="button" class="btn btn-sm btn-warn" data-bs-container="body"
                            data-bs-toggle="popover" data-bs-placement="right"
                            data-bs-trigger="focus"
                            data-bs-html="true"
                            title="A valid email address has four parts:"
                            data-bs-content="
                            <ul>
                                <li>Recipient name</li>
                                <li>@ symbol</li>
                                <li>Domain name</li>
                                <li>Top-level domain</li>
                            </ul>"
                            onfocus="showPopover(this)">
                        ?
                    </button>
                </div>
            </div>

            <%-- PASSWORD --%>
            <div class="mb-1">
                <label for="password" class="form-label">
                    <fmt:message key='auth_jsp.password'/>
                </label>
                <div class="input-group mb-3">
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Password" aria-describedby="passwordHelpBlock"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                    <button type="button" class="btn btn-sm btn-warn" data-bs-container="body"
                            data-bs-toggle="popover" data-bs-placement="right"
                            data-bs-trigger="focus"
                            data-bs-html="true"
                            title="Your password must:"
                            data-bs-content="
                        <ul>
                            <li>Contain at least 8 characters</li>
                            <li>contain at least 1 number</li>
                            <li>contain at least 1 lowercase character (a-z)</li>
                            <li>contain at least 1 uppercase character (A-Z)</li>
                            <li>contains only 0-9a-zA-Z</li>
                        </ul>"
                            onfocus="showPopover(this)">
                        ?
                    </button>
                </div>
            </div>

            <%-- PASSWORD2 --%>
            <div class="mb-1">
                <label for="password2" class="form-label">
                    <fmt:message key='auth_jsp.password2'/>
                </label>
                <div class="input-group mb-3">
                    <input type="password" class="form-control" id="password2" name="password2"
                           placeholder="Password again"
                           aria-describedby="passwordHelpBlock"
                           data-bs-toggle="popover" data-bs-trigger="manual"
                           data-bs-placement="bottom" data-ds-container="body"
                           data-bs-delay='{"show":100,"hide":300}' data-bs-title=" "
                           onchange="validate(this)"
                           required>
                </div>
                <div id="passwordHelpBlock" class="form-text _main-color2">
                    Your password must be 8-20 characters long, contain letters and numbers,
                    and must not contain spaces, special characters, or emoji.
                </div>

                <%-- BUTTON Register --%>
                <form action="?" method="POST">
                    <div class="g-recaptcha" data-sitekey="6LfXXKwiAAAAAHo-hVUm0ap9z-hjBFzCA7wgOA5o"
                         data-callback="successCaptcha"></div>
                    <button type="submit" id="register_submit_button" disabled
                            class="btn mt-3 _btn_form <customTags:access role="authorized" modifier="disabled"/>">
                        <fmt:message key='auth_jsp.btn.register'/>
                    </button>
                </form>


<%--                <div class="form-group">--%>
<%--                    <button id="register_submit_button" type="submit"--%>
<%--                            onclick="submitRegistrationForm();"--%>
<%--                            class="btn mt-3 _btn_form <customTags:access role="authorized" modifier="disabled"/>">--%>
<%--                        <fmt:message key='auth_jsp.btn.register'/>--%>
<%--                    </button>--%>
<%--                </div>--%>
            </div>

        </form>
    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>