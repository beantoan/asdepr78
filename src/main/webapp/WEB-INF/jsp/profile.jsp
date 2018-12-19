<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:default>
    <jsp:attribute name="title">
      Profile
    </jsp:attribute>

    <jsp:body>

        <div class="card form-signin-container">
            <div class="card-body">
                <form:form method="POST" class="form-register needs-validation" action="/profile"
                           modelAttribute="user" novalidate="true">
                    <h1 class="h3 mb-3 font-weight-normal">Your Information</h1>

                    <t:message warning="${warning}" success="${success}"></t:message>

                    <div class="row">
                        <div class="col-sm-12 col-md-6 row">
                            <div class="col-md-12 col-lg-4">
                                <span class="input-group-text">Full Name</span>
                            </div>

                            <div class="col-md-12 col-lg-8">
                                <input class="form-control pl-2" disabled value="${user.fullName}">
                            </div>
                        </div>
                        <div class="col-sm-12 col-md-6 row">
                            <div class="col-md-12 col-lg-4">
                                <span class="input-group-text">Email</span>
                            </div>
                            <div class="col-md-12 col-lg-8">
                                <input class="form-control pl-2" disabled value="${user.email}">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12 col-md-6 row">
                            <div class="col-md-12 col-lg-4">
                                <span class="input-group-text">New Password</span>
                            </div>

                            <div class="col-md-12 col-lg-8">
                                <form:input type="password" path="password"
                                            class="form-control" value=""/>
                                <small class="text-muted">Leave empty if you don't want to change</small>
                            </div>
                        </div>
                        <div class="col-sm-12 col-md-6 row">
                            <div class="col-md-12 col-lg-4">
                                <span class="input-group-text">Confirm Password</span>
                            </div>

                            <div class="col-md-12 col-lg-8">
                                <form:input type="password" path="confirmPassword"
                                            class="form-control" value=""/>
                            </div>
                        </div>
                    </div>

                    <br/>

                    <div class="d-flex justify-content-center">
                        <button class="btn btn-raised btn-primary" type="submit">Save</button>
                    </div>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:default>