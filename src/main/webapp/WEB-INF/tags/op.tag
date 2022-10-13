<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="x" required="true" type="java.lang.Integer" %>
<%@ attribute name="y" required="true" type="java.lang.Integer" %>
<%@ attribute name="op" required="true" type="java.lang.String" %>

<c:choose>
	<c:when test="${op == '+'}">${x + y}</c:when>
	<c:when test="${op == '-'}">${x - y}</c:when>
	<c:when test="${op == '/'}">${x / y}</c:when>
	<c:otherwise>No such operation!!!</c:otherwise>
</c:choose> 