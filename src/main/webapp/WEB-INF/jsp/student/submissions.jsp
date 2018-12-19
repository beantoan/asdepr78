<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unical.asde.pr78.entity.Submission" %>

<t:default>
    <jsp:attribute name="title">
      Started Exams
    </jsp:attribute>

    <jsp:body>
        <t:message warning="${warning}"></t:message>

        <div class="row justify-content-center">
            <c:forEach var="submission" items="${submissions}">
                <div class="card m-3 stu-exam-card">
                    <h5 class="card-header ${submission.status eq Submission.STATUS_STARTED ? 'bg-warning' : (submission.status eq Submission.STATUS_REVIEWED ? 'bg-primary' : '')}">
                            ${submission.exam.title}
                    </h5>

                    <div class="card-body d-flex flex-column justify-content-between">
                        <div>
                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">face</i>
                                <div>
                                    By ${submission.exam.professor.fullName}
                                </div>
                            </div>

                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">autorenew</i>
                                <div>
                                        ${submission.statusLabel}
                                </div>
                            </div>

                            <c:if test="${submission.status eq Submission.STATUS_REVIEWED}">
                                <div class="d-flex align-items-center">
                                    <i class="material-icons mr-1">stars</i>
                                    <div class="font-weight-bold">Score ${submission.point} points</div>
                                </div>

                                <div class="d-flex align-items-center text-primary">
                                    <i class="material-icons mr-1">check_circle_outline</i>
                                    <div>${submission.correctCount} Correct Answers</div>
                                </div>

                                <div class="d-flex align-items-center text-danger">
                                    <i class="material-icons mr-1">error_outline</i>
                                    <div>${submission.incorrectCount} Incorrect Answers</div>
                                </div>
                            </c:if>

                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">alarm</i>
                                <div>
                                    Started <fmt:formatDate value="${submission.startedAt}"
                                                            pattern="HH:mm:ss dd-MM-yyyy"/>
                                    <br/>

                                    <c:if test="${not empty submission.finishedAt}">
                                        Finished <fmt:formatDate value="${submission.finishedAt}"
                                                                 pattern="HH:mm:ss dd-MM-yyyy"/>
                                    </c:if>

                                    <c:if test="${submission.status eq Submission.STATUS_STARTED}">
                                        <c:if test="${submission.remainingTime gt 0}">
                                            Remaining ${submission.remainingTime} minutes
                                        </c:if>

                                        <c:if test="${submission.remainingTime le 0}">
                                            Timeout
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <c:if test="${submission.status eq Submission.STATUS_STARTED and submission.remainingTime gt 0}">
                            <a href="/student/exams/start/${submission.exam.id}"
                               class="btn btn-outline-primary btn-block mt-3">
                                Continue Exam
                            </a>
                        </c:if>

                        <c:if test="${submission.status eq Submission.STATUS_REVIEWED}">
                            <a href="/student/results/appeal/${submission.exam.id}"
                               class="btn btn-outline-primary btn-block mt-3">
                                Appeal Result
                            </a>
                        </c:if>

                    </div>
                </div>
            </c:forEach>
            <div class="card m-3 stu-exam-card invisible"></div>
            <div class="card m-3 stu-exam-card invisible"></div>
        </div>
    </jsp:body>
</t:default>