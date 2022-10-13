<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="locale" required="true" type="java.lang.String" %>

<c:choose>
	<c:when test="${empty locale}">
		<c:if test="${empty currentLocale}">
			<c:set var="currentLocale" value="en" scope="session"/>
		</c:if>
		<c:if test="${not empty currentLocale}">
			<c:set var="currentLocale" value="${currentLocale}" scope="session"/>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="currentLocale" value="${locale}" scope="session"/>
	</c:otherwise>
</c:choose>

<fmt:setLocale value="${currentLocale}" scope="session"/>
<fmt:setBundle basename="resources"/>
