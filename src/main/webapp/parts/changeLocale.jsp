<%@ include file="/parts/head.jspf" %>

<tagFile:checkLocale locale="${param.locale}"/>

<c:if test = "${empty param.url}"><c:redirect url="/"/></c:if>
<c:if test = "${not empty param.url}"><c:redirect url="${param.url}"/></c:if>
