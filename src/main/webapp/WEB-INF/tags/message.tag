<%@attribute name="warning" required="false" type="java.lang.String" %>
<%@attribute name="success" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty warning}">
    <div class="alert alert-danger" role="alert">
            ${warning}
    </div>
</c:if>

<c:if test="${not empty success}">
    <div class="alert alert-success" role="alert">
            ${success}
    </div>
</c:if>