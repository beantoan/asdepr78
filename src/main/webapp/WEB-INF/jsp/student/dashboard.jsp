<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:default>
    <jsp:attribute name="title">
      Dashboard
    </jsp:attribute>

    <jsp:body>
        <div class="jumbotron">

            <div class="row">
                Hello <sec:authentication property="principal.username"/>. You are a student.
            </div>
        </div>
    </jsp:body>
</t:default>