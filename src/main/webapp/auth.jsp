<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>Railway ticket office - Login</title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <%--    <c:if test="${actionName == 'login'}">--%>
    <%--        <h3>${actionName}</h3>--%>
    <%--&lt;%&ndash;        <% session.getAttribute("actionName"); %>&ndash;%&gt;--%>
    <%--    </c:if>--%>
<%--    ${actionName == null ? 'some text when true' : 'some text when false'}--%>


<%--    actionName=${actionName}--%>
<%--    isAuthorized=${isAuthorized}--%>

    <c:choose>
        <c:when test="${(isAuthorized == null || isAuthorized == false) && actionName != 'logout'}">

            <div class="ms-5 mt-5 h3 _main-color1">
                Log in to Your Account
            </div>

            <%--<form class="m-3 w-50" action="/auth" method="POST">--%>
            <form class="ms-5 mt-4 border border-1 rounded _form _main-bg-color4 _main-color2 p-3 shadow-lg"
                  method="POST" action="/auth">

                <div class="mb-1">
                    <label for="name" class="form-label">Username</label>
                    <input type="text" class="form-control" placeholder="Username" aria-label="name" id="name"
                           name="name">
                </div>

                <div class="mb-1">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password">
                </div>

                <input type="hidden" name="actionName" value="login"/>
                    <%--        <c:if test="${empty actionName}">--%>
                    <%--            <% session.setAttribute("actionName", "login"); %>--%>
                    <%--        </c:if>--%>

                <div class="form-group">
                    <button type="submit" class="btn mt-3">Login</button>
                </div>

            </form>

        </c:when>

<%--        <c:when test="${actionName == 'login'}">--%>

<%--&lt;%&ndash;            <div class="fs-3 m-5">You are authorized!</div>&ndash;%&gt;--%>
<%--            <%@ include file="/parts/message.jspf" %>--%>

<%--        </c:when>--%>

        <c:when test="${actionName == 'logout'}">

<%--            <div class="fs-3 m-5">You are authorized!</div>--%>
<%--            <%@ include file="/parts/message.jspf" %>--%>

            <% session.setAttribute("actionName", "login"); %>
        </c:when>

        <c:otherwise>
<%--            else...${actionName}--%>
        </c:otherwise>
    </c:choose>

    <%--        <c:when test="${session.getAttribute('actionName') == 'login'">login</c:when> <!-- else if condition -->--%>


    <%--    <c:if test="${not empty actionName}">--%>
    <%--        <h3>${actionName}</h3>--%>
    <%--    </c:if>--%>

<%--    <c:if test="${not empty errors}">--%>
<%--&lt;%&ndash;        <div class="alert alert-danger m-5 shadow alert-dismissible fade show" role="alert">&ndash;%&gt;--%>
<%--&lt;%&ndash;            <i class="fa fa-exclamation-triangle fa-2xl me-2"></i>&ndash;%&gt;--%>
<%--&lt;%&ndash;                ${errors}&ndash;%&gt;--%>
<%--&lt;%&ndash;            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </div>&ndash;%&gt;--%>
<%--    </c:if>--%>

<%--    <c:if test="${not empty messages}">--%>
<%--&lt;%&ndash;        <div class="alert alert-light m-5 shadow alert-dismissible fade show" role="alert">&ndash;%&gt;--%>
<%--&lt;%&ndash;            <i class="fa fa-info-circle fa-2xl me-2"></i>&ndash;%&gt;--%>
<%--&lt;%&ndash;                ${messages}&ndash;%&gt;--%>
<%--&lt;%&ndash;            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </div>&ndash;%&gt;--%>
<%--    </c:if>--%>


    <%@ include file="/parts/message.jspf" %>


    <%--    <h3>name: ${name}</h3>--%>
    <%--    <h3>password: ${password}</h3>--%>


    <%@ include file="/parts/footer.jspf" %>

</div>


<%@ include file="/parts/body_bottom.jspf" %>


<%--onclick="handleClick(this.id);" id="customerId"--%>
<%--<input type="hidden" name="tableValue" id="tableTextId" />--%>

<%--<%@ include file="/parts/body.jsp"/>--%>
<%--<script type="text/javascript">--%>
<%--    function handleClick(clickedId)--%>
<%--    {--%>
<%--        if(clickedId == "customerId")--%>
<%--            document.getElementById('tableTextId').value = "customer";--%>
<%--        else--%>
<%--            document.getElementById('tableTextId').value = "company";--%>
<%--    }--%>
<%--</script>--%>


<%--<script>--%>
<%--    setTimeout(function() {--%>
<%--        document.location = "/next/page/to/go/to.jsp";--%>
<%--    }, 1000); // <-- this is the delay in milliseconds--%>
<%--</script>--%>


</body>
</html>