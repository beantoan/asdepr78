<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unical.asde.pr78.entity.Submission" %>

<t:default>
    <jsp:attribute name="title">
      Submissions
    </jsp:attribute>

    <jsp:body>

        <div class="row mb-3 d-flex justify-content-between">
            <div class="d-flex justify-content-start h3">
                <c:if test="${not empty exam}">
                    ${exam.title}
                </c:if>
            </div>
            <div class="d-flex justify-content-end">
                <a class="btn btn-outline-primary d-flex align-items-center" href="/professor/exams">
                    <i class="material-icons mr-1">undo</i>Back Exams
                </a>
            </div>
        </div>

        <t:message warning="${warning}"></t:message>

        <c:if test="${not empty submissions}">
            <div class="row">
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Full Name</th>
                        <th scope="col">Status</th>
                        <th scope="col">Score</th>
                        <th scope="col">Correct</th>
                        <th scope="col">Not Correct</th>
                        <th scope="col">Submitted at</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="submission" items="${submissions}" varStatus="counter">
                        <tr>
                            <th>${counter.index + 1}</th>
                            <td>${submission.student.fullName}</td>
                            <td>${submission.statusLabel}</td>
                            <td>${submission.point}</td>
                            <td>${submission.correctCount}</td>
                            <td>${submission.incorrectCount}</td>
                            <td>
                                <fmt:formatDate value="${submission.finishedAt}"
                                                pattern="HH:mm:ss dd-MM-yyyy"/>
                            </td>
                            <td>
                                <c:if test="${submission.status eq Submission.STATUS_SUBMITTED or submission.status eq Submission.STATUS_REVIEWING}">
                                    <a href="/professor/submissions/review/${exam.id}/${submission.id}"
                                       class="btn btn-primary btn-block btn-sm btn-block">
                                        Review
                                    </a>
                                </c:if>

                                <c:if test="${submission.status eq Submission.STATUS_REVIEWED}">
                                    <a href="/professor/submissions/view/${exam.id}/${submission.id}"
                                       class="btn btn-primary btn-block btn-sm btn-block">
                                        View
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </jsp:body>
</t:default>