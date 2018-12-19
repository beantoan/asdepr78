<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:login>
    <jsp:attribute name="title">
      Login
    </jsp:attribute>

    <jsp:body>
        <div class="card form-signin-container">
            <div class="card-body">
                <form:form method="POST" class="col-sm needs-validation"
                           action="/login" modelAttribute="user" novalidate="true">
                    <h1 class="h3 mb-3 font-weight-normal">Online Examination System</h1>

                    <t:message warning="${warning}" success="${success}"></t:message>

                    <div class="bmd-form-group">
                        <label for="inputEmail" class="bmd-label-floating">Email</label>
                        <div class="input-group">
                            <form:input type="text" path="email" id="inputEmail" class="form-control" required="true"/>
                            <div class="invalid-feedback">
                                Please provide your email
                            </div>
                        </div>
                    </div>

                    <div class="bmd-form-group">
                        <label for="inputPassword" class="bmd-label-floating">Password</label>
                        <div class="input-group">
                            <form:input type="password" path="password" id="inputPassword" class="form-control"
                                        required="true"/>
                            <div class="invalid-feedback">
                                Please provide your password
                            </div>
                        </div>
                    </div>

                    <br/>

                    <button class="btn btn-lg btn-block btn-raised btn-primary" type="submit">Login</button>
                </form:form>

                <div class="col-sm text-center">
                    <a href="/register" role="button">Register new account</a>
                </div>
            </div>
        </div>
    </jsp:body>
</t:login>