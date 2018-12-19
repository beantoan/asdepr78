<%@tag description="Login page template" pageEncoding="UTF-8"%>
<%@attribute name="title" fragment="true" %>
<%@attribute name="script" fragment="true" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="currentUrl" value="${requestScope['javax.servlet.forward.request_uri']}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>
        <jsp:invoke fragment="title"/>
    </title>

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons">
    <link rel="stylesheet"
          href="https://unpkg.com/bootstrap-material-design@4.1.1/dist/css/bootstrap-material-design.min.css"
          integrity="sha384-wXznGJNEXNG1NFsbm0ugrLFMQPWswR3lds2VeinahP8N0zJw9VWSopbjv2x7WCvX" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.1/css/all.css"
          integrity="sha384-gfdkjb5BdAXd+lj+gudLWI+BXq4IuLW5IT+brZEZsLFm++aCMlF1V92rMkPaX4PP" crossorigin="anonymous">

    <link rel="stylesheet" type="text/css" href="/css/waitMe.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>

<div class="bmd-layout-container bmd-drawer-f-l bmd-drawer-overlay">

    <div class="d-none d-lg-block">
        <nav class="navbar navbar-expand-md navbar-dark bg-primary bg-faded">

            <a class="navbar-brand d-flex align-items-center">
                <img class="logo rounded" src="/img/logo_160.png"/>
                <div class="text-light">OES</div>
            </a>

            <ul class="navbar-nav mr-auto">
                <sec:authorize access="hasRole('ROLE_PROFESSOR')">
                    <li class="nav-item ${currentUrl eq '/professor' ? 'active' : ''}">
                        <a class="nav-link" href="/professor">Dashboard</a>
                    </li>

                    <li class="nav-item ${fn:startsWith(currentUrl, '/professor/exams') or
                    fn:startsWith(currentUrl, '/professor/submissions') ? 'active' : ''}">
                        <a class="nav-link" href="/professor/exams">Exams</a>
                    </li>

                    <%--<li class="nav-item">--%>
                    <%--<a class="nav-link" href="/professor/report">Report</a>--%>
                    <%--</li>--%>

                    <li class="nav-item">
                        <a class="btn btn-light btn-raised text-primary ml-3" href="/professor/exams/create">
                            Create Exam
                        </a>
                    </li>
                </sec:authorize>

                <sec:authorize access="hasRole('ROLE_SECRETARY')">
                    <li class="nav-item">
                        <a class="nav-link" href="/secretary">Dashboard</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="/secretary/professors">Professors</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="/secretary/students">Students</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="/secretary/report">Report</a>
                    </li>
                </sec:authorize>

                <sec:authorize access="hasRole('ROLE_STUDENT')">
                    <li class="nav-item ${currentUrl eq '/student' ? 'active' : ''}">
                        <a class="nav-link" href="/student">Dashboard</a>
                    </li>

                    <li class="nav-item ${fn:startsWith(currentUrl, '/student/submissions') ? 'active' : ''}">
                        <a class="nav-link" href="/student/submissions">Submissions</a>
                    </li>

                    <li class="nav-item ${fn:startsWith(currentUrl, '/student/exams') ? 'active' : ''}">
                        <a class="nav-link" href="/student/exams">Exams</a>
                    </li>

                    <%--<li class="nav-item">--%>
                    <%--<a class="nav-link" href="/student/report">Report</a>--%>
                    <%--</li>--%>
                </sec:authorize>
            </ul>

            <ul class="navbar-nav navbar-right">
                <li class="nav-item ${currentUrl eq '/profile' ? 'active' : ''}">
                    <a class="nav-link" href="/profile">Profile</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/logout">
                        <i class="fas fa-sign-out-alt"></i>&nbsp;Exit
                    </a>
                </li>
            </ul>
        </nav>
    </div>

    <header class="bmd-layout-header d-block d-lg-none">
        <div class="navbar navbar-dark bg-primary bg-faded">
            <button class="navbar-toggler" type="button" data-toggle="drawer" data-target="#togger-drawer">
                <span class="sr-only">Toggle drawer</span>
                <i class="material-icons">menu</i>
            </button>
            <ul class="nav navbar-nav">
                <li class="nav-item">Online Examination System</li>
            </ul>
        </div>
    </header>

    <div id="togger-drawer" class="bmd-layout-drawer bg-faded d-block d-lg-none">
        <header>
            <div class="media d-flex align-items-center">
                <img class="mr-1 logo-mobile" src="/img/logo_160.png">
                <div class="media-body">
                    <h5 class="mt-0">OES</h5>
                    <sec:authentication property="principal.username"/>
                </div>
            </div>
        </header>

        <ul class="list-group">
            <sec:authorize access="hasRole('ROLE_PROFESSOR')">
                <a class="list-group-item" href="/professor">
                    <i class="material-icons">home</i>
                    Dashboard
                </a>

                <a class="list-group-item" href="/professor/exams">
                    <i class="material-icons">list</i>
                    Exams
                </a>

                <%--<a class="list-group-item" href="/professor/report">--%>
                <%--<i class="material-icons">bar_chart</i>--%>
                <%--Report--%>
                <%--</a>--%>
            </sec:authorize>

            <sec:authorize access="hasRole('ROLE_SECRETARY')">
                <a class="list-group-item" href="/secretary">
                    <i class="material-icons">home</i>
                    Dashboard
                </a>

                <a class="list-group-item" href="/secretary/professors">
                    <i class="material-icons">list</i>
                    Professors
                </a>

                <a class="list-group-item" href="/secretary/students">
                    <i class="material-icons">list</i>
                    Students
                </a>

                <a class="list-group-item" href="/secretary/report">
                    <i class="material-icons">bar_chart</i>
                    Report
                </a>
            </sec:authorize>

            <sec:authorize access="hasRole('ROLE_STUDENT')">
                <a class="list-group-item" href="/student">
                    <i class="material-icons">home</i>
                    Dashboard
                </a>

                <a class="list-group-item" href="/student/submissions">
                    <i class="material-icons">list</i>
                    Submissions
                </a>

                <a class="list-group-item" href="/student/exams">
                    <i class="material-icons">list</i>
                    Exams
                </a>

                <%--<a class="list-group-item" href="/student/report">--%>
                <%--<i class="material-icons">bar_chart</i>--%>
                <%--Report--%>
                <%--</a>--%>
            </sec:authorize>
        </ul>

        <hr/>

        <ul class="list-group">
            <a class="list-group-item" href="/profile">
                <i class="material-icons">security</i>
                Profile
            </a>
            <a class="list-group-item" href="/logout">
                <i class="material-icons">exit_to_app</i>
                Exit
            </a>
        </ul>
    </div>

    <main class="bmd-layout-content">
        <div class="container pt-5 pb-5">
            <jsp:doBody/>
        </div>
    </main>
</div>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://cdn.rawgit.com/FezVrasta/snackbarjs/1.1.0/dist/snackbar.min.js"></script>
<script src="https://unpkg.com/bootstrap-material-design@4.1.1/dist/js/bootstrap-material-design.js"
        integrity="sha384-CauSuKpEqAFajSpkdjv3z9t8E7RlpJ1UP0lKM/+NdtSarroVKu069AlsRPKkFBz9"
        crossorigin="anonymous"></script>

    <script src="/js/logger.js"></script>
    <script src="/js/waitMe.min.js"></script>
<script src="/js/common.js"></script>

<sec:authorize access="hasRole('ROLE_SECRETARY')">
    <script src="/js/secretary.js"></script>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_PROFESSOR')">
    <script src="/js/professor.js"></script>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_STUDENT')">
    <script src="/js/student.js"></script>
</sec:authorize>

    <jsp:invoke fragment="script"/>
</body>
</html>