<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <%@ include file="/parts/head.jspf" %>
    <title>
        <fmt:message key='index_jsp.title'/>
    </title>
</head>
<body>

<div class="_wrapper">
    <%@ include file="/parts/header.jspf" %>

    <div class="d-flex flex-column">

        <div class="m-3 _main-content">

            <div class="display-5 m-2 w-90 text-center _main-color1 ">
                <fmt:message key='index_jsp.welcome.head1'/>
            </div>

            <div class="m-5">

                <div class="display-6 mb-5 text-center border-1 border-dark">
                    <fmt:message key='index_jsp.welcome.head2'/>
                </div>

                <div class="h5 mb-5">
                    <div class="mb-4">
                        <a href="/search">
                            <i class="fa fa-search fa-2xl me-2"></i>
                            <fmt:message key='index_jsp.welcome.head3'/>
                        </a>
                    </div>

                    <ul>
                        <li><fmt:message key='index_jsp.welcome.text1'/></li>
                        <li><fmt:message key='index_jsp.welcome.text2'/></li>
                        <li><fmt:message key='index_jsp.welcome.text3'/></li>
                        <li><fmt:message key='index_jsp.welcome.text4'/></li>
                        <li><fmt:message key='index_jsp.welcome.text5'/></li>
                        <li><fmt:message key='index_jsp.welcome.text6'/></li>
                    </ul>
                    <fmt:message key='index_jsp.welcome.text7'/>
                </div>

                <c:if test="${isAuthorized != true}">
                    <div>
                        <span class="badge">!</span> <fmt:message key='index_jsp.welcome.text_bottom1'/>
                        <a href="/login"><fmt:message key='index_jsp.welcome.text_bottom2'/></a>
                        <fmt:message key='index_jsp.welcome.text_bottom3'/>
                    </div>
                </c:if>

            </div>
        </div>
    </div>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/body_bottom.jspf" %>

</body>
</html>