<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<t:login>
    <jsp:attribute name="title">
      Register
    </jsp:attribute>

    <jsp:body>

        <div class="card form-signin-container">
            <div class="card-body">
                <form:form method="POST" class="form-register needs-validation" action="/register"
                           modelAttribute="user" novalidate="true">
                    <h1 class="h3 mb-3 font-weight-normal">Online Examination System</h1>

                    <div class="bmd-form-group">
                        <label for="inputFullName" class="bmd-label-floating">Full name</label>
                        <div class="input-group">
                            <form:input type="text" path="fullName" id="inputFullName" class="form-control"
                                        required="true"/>
                            <div class="invalid-feedback">
                                Please provide your name
                            </div>
                        </div>
                    </div>

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

                    <div class="bmd-form-group">
                        <label for="inputRoles">Role</label>
                        <div class="input-group">
                            <form:select path="roles" id="inputRoles" cssClass="form-control"
                                         multiple="false" required="true">
                                <form:option value="" label="Select a Role"/>
                                <form:options items="${roles}" itemValue="id" itemLabel="desc"/>
                            </form:select>

                            <div class="invalid-feedback">
                                Please select a role
                            </div>
                        </div>
                    </div>

                    <br/>

                    <button class="btn btn-lg btn-block btn-raised btn-primary" type="submit">Register</button>
                </form:form>

                <div class="col-sx-12 text-center">
                    <a href="/login" role="button">Login system</a>
                </div>
            </div>
        </div>
    </jsp:body>
</t:login>