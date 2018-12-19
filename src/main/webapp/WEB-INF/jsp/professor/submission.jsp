<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="it.unical.asde.pr78.entity.Submission" %>

<t:default>
    <jsp:attribute name="title">
      Review Submission
    </jsp:attribute>

    <jsp:attribute name="script">
        <script type="text/javascript">
            $(document).ready(function () {
                Professor.reviewSubmission();
            });
        </script>
    </jsp:attribute>

    <jsp:body>

        <div class="row mb-3 d-flex justify-content-between">
            <div class="d-flex justify-content-start h3">
                <c:if test="${not empty student}">
                    By ${student.fullName}
                </c:if>
            </div>
            <div class="d-flex justify-content-end">
                <c:if test="${exam != null}">
                    <a class="btn btn-outline-primary d-flex align-items-center"
                       href="/professor/submissions/${exam.id}">
                        <i class="material-icons mr-1">undo</i>Back Submissions
                    </a>
                </c:if>
            </div>
        </div>

        <t:message warning="${warning}"></t:message>

        <div class="row justify-content-center">
            <c:if test="${exam != null}">
                <div class="card mb-3 w-100">
                    <h4 class="card-header">${exam.title} (${fn:length(exam.questions)} questions)</h4>
                    <div class="card-body">
                        <p class="card-text">${exam.desc}</p>

                        <div class="row h5 ml-1 mr-2 d-flex justify-content-between">
                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">stars</i>
                                <div>Score ${submission.point} points</div>
                            </div>
                            <div class="d-flex align-items-center text-primary">
                                <i class="material-icons mr-1">check_circle_outline</i>
                                <div>${submission.correctCount} Correct Answers</div>
                            </div>
                            <div class="d-flex align-items-center text-danger">
                                <i class="material-icons mr-1">error_outline</i>
                                <div>${submission.incorrectCount} Incorrect Answers</div>
                            </div>
                        </div>
                    </div>
                </div>

                <c:forEach var="question" items="${questions}">

                    <div class="card mb-3 w-100 submission-card">
                        <h4 class="card-header ${question.answersReviewed ? (question.answersCorrect ? 'text-light bg-primary' : 'text-light bg-warning') : ''}">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex justify-content-start align-items-center">
                                    <i class="material-icons mr-2">help_outline</i>
                                    <div class="">${question.title} (${question.point} points)</div>
                                </div>
                                <div class="d-flex justify-content-end">
                                    <c:if test="${question.answersReviewed}">
                                        <span>${question.answersCorrect ? 'Correct' : 'Incorrect'}</span>
                                    </c:if>
                                </div>
                            </div>
                        </h4>

                        <div class="card-body">
                            <c:if test="${not empty question.choices}">
                                <div class="pl-4 pr-4">
                                    <div class="row">
                                        <c:forEach var="choice" items="${question.choices}" varStatus="counter">
                                            <div class="col-sm-12 col-md-6">${counter.index + 1}. ${choice.title}</div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>

                            <div class="mt-3 pl-4 pr-4 row">
                                <div class="col-sm-12 col-md-6">
                                    <div class="font-weight-bold">Answer</div>
                                    <blockquote class="blockquote">
                                            ${question.answerValue}
                                    </blockquote>
                                </div>
                                <c:if test="${not empty question.solutionValue}">
                                    <div class="col-sm-12 col-md-6">
                                        <div class="font-weight-bold">Solution</div>
                                        <blockquote class="blockquote">
                                                ${question.solutionValue}
                                        </blockquote>
                                    </div>
                                </c:if>
                            </div>

                            <c:if test="${submission.status eq Submission.STATUS_REVIEWING and
                            not empty question.answers and not question.answersReviewed}">
                                <div class="row d-flex justify-content-end mt-3 mr-3">
                                    <button class="btn btn-outline-danger button-answer-as-incorrect mr-2"
                                            url="/professor/apis/answerAsIncorrect/${exam.id}/${submission.id}/${question.id}">
                                        Not Correct
                                    </button>

                                    <button class="btn btn-outline-primary button-answer-as-correct"
                                            url="/professor/apis/answerAsCorrect/${exam.id}/${submission.id}/${question.id}">
                                        Correct
                                    </button>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </c:forEach>

                <c:if test="${submission.status eq Submission.STATUS_REVIEWING}">
                    <div class="row d-flex justify-content-center bg-light bg-faded pt-2 pb-2 align-items-center">
                        <button class="btn btn-lg btn-block btn-raised btn-primary button-submission-as-reviewed"
                                type="submit" url="/professor/apis/submissionAsReviewed/${exam.id}/${submission.id}">
                            Mark as reviewed
                        </button>
                    </div>
                </c:if>
            </c:if>
        </div>

    </jsp:body>

</t:default>