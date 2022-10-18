<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<tagFile:checkLocale locale="${param.locale}"/>

<c:if test = "${empty param.url}"><c:redirect url="/"/></c:if>
<c:if test = "${not empty param.url}"><c:redirect url="${param.url}"/></c:if>
