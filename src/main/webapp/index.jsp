<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/parts/init.jspf" %>

<!DOCTYPE html>
<html lang="en">
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

            <div class="display-6 m-2 w-90 text-center _main-color4 h-2 mt-5 fw-bold">
                <fmt:message key='index_jsp.welcome.head1'/>
            </div>

            <div class="m-5">

                <div class="display-6 mb-5 text-center _main-color4">
                    <fmt:message key='index_jsp.welcome.head2'/>
                </div>

                <div class="h5 mb-5">
                    <div class="mb-4">
                        <a href="/search">
                            <em class="fa fa-search fa-2xl me-2"></em>
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

                    <div class="<customTags:access role="authorized" modifier="d-none"/>">
                        <span class="badge">!</span>
                        <fmt:message key='index_jsp.welcome.text_bottom1'/>
                        <a href="/login">
                            <fmt:message key='index_jsp.welcome.text_bottom2'/>
                        </a>
                        <fmt:message key='index_jsp.welcome.text_bottom3'/>
                    </div>

            </div>
        </div>
    </div>

    <%@ include file="/parts/footer.jspf" %>

</div>

<%@ include file="/parts/bodyBottom.jspf" %>

</body>
</html>