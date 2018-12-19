<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:default>
    <jsp:attribute name="title">
      Dashboard
    </jsp:attribute>

    <jsp:body>
        <div class="jumbotron">

            <div class="row">
                Hello, I'm a secretary
            </div>
        </div>
    </jsp:body>
</t:default>