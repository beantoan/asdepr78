<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="it.unical.asde.pr78.entity.Question" %>

<t:default>
    <jsp:attribute name="title">
      Start Exam
    </jsp:attribute>

    <jsp:body>

        <t:message warning="${warning}"></t:message>

        <div class="row justify-content-center">
            <c:if test="${exam != null}">
                <form:form method="POST" action="/student/exams/submit/${exam.id}" modelAttribute="answerForm"
                           autocomplete="off" cssClass="w-100">

                    <div class="card mb-3 w-100">
                        <h4 class="card-header">${exam.title} (${fn:length(exam.questions)} questions)</h4>
                        <div class="card-body">
                            <p class="card-text">${exam.desc}</p>
                        </div>
                    </div>

                    <c:forEach var="question" items="${exam.questions}">

                        <div class="card mb-3 w-100">
                            <div class="card-body">
                                <h6 class="card-title">
                                    <div class="d-flex align-items-center">
                                        <i class="material-icons mr-2">help_outline</i>
                                        <div>${question.title} (${question.point} points)</div>
                                    </div>
                                </h6>

                                <div class="pl-4 pr-4">
                                    <c:if test="${question.type eq Question.TYPE_SHORT_ANSWER}">
                                        <div class="form-group">
                                            <label>Your answer:</label>
                                            <form:input path="answers[${question.id}]" maxlength="255"
                                                        cssClass="form-control" autocomplete="off"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${question.type eq Question.TYPE_PARAGRAPH}">
                                        <div class="form-group">
                                            <label>Your answer:</label>
                                            <form:textarea path="answers[${question.id}]" maxlength="255"
                                                           cssClass="form-control" rows="2"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${question.type eq Question.TYPE_MULTI_CHOICE}">
                                        <c:forEach var="choice" items="${question.choices}">
                                            <div class="radio">
                                                <label>
                                                    <form:radiobutton path="answers[${question.id}]"
                                                                      value="${choice.id}"/> ${choice.title}
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${question.type eq Question.TYPE_CHECKBOXES}">
                                        <c:forEach var="choice" items="${question.choices}">
                                            <div class="checkbox">
                                                <label>
                                                    <form:checkbox path="answers[${question.id}]"
                                                                   value="${choice.id}"/> ${choice.title}
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${question.type eq Question.TYPE_DROPDOWN}">
                                        <div class="form-group">
                                            <form:select path="answers[${question.id}]" cssClass="form-control"
                                                         multiple="false">
                                                <form:option value="" label="(Select an option)"/>
                                                <form:options items="${question.choices}" itemValue="id"
                                                              itemLabel="title"/>
                                            </form:select>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="row fixed-bottom d-flex justify-content-center bg-light bg-faded pt-2 pb-2 d-flex align-items-center">
                        <div class="mr-3 d-flex align-items-center">
                            <div><i class="material-icons">alarm</i></div>
                            <div><h4 id="exam-duration" class="font-weight-bold ml-1">00:00:00</h4></div>
                            <div id="exam-duration-value" class="d-none">${duration}</div>
                        </div>
                        <div class="ml-3">
                            <button class="btn btn-block btn-raised btn-primary" type="submit">Submit Answers</button>
                        </div>
                    </div>
                </form:form>
            </c:if>
        </div>

    </jsp:body>
</t:default>