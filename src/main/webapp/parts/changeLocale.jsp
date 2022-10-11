<%@ include file="/parts/head.jspf" %>

<fmt:setLocale value="${param.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<c:set var="currentLocale" value="${param.locale}" scope="session"/>
<c:redirect url="${param.url}"/>
