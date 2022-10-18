<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

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
            <div id="passwordHelpBlock" class="form-text _main-color2">
                Your password must be 8-20 characters long, contain letters and numbers,
                and must not contain spaces, special characters, or emoji.

            </div>

            <div class="form-group">
                <button type="submit" id="submit_button"
                        class="btn mt-3 _btn_form <customTags:access role="authorized" modifier="disabled"/>">
                    <fmt:message key='auth_jsp.btn.login'/>
                </button>
            </div>

        </form>
    </div>

    <%@ include file="/parts/message.jspf" %>
    <%@ include file="/parts/footer.jspf" %>
</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>