<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:default>
    <jsp:attribute name="title">
      ${empty exam.id ? 'Create Exam' : (exam.editable ? "Edit Exam" : 'View Exam')}
    </jsp:attribute>

    <jsp:attribute name="script">
        <link rel="stylesheet" type="text/css"
              href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/css/bootstrap-material-datetimepicker.min.css"/>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/js/bootstrap-material-datetimepicker.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                Professor.createExam();
            });
        </script>
    </jsp:attribute>

    <jsp:body>

        <div class="row mb-3 d-flex justify-content-between">
            <div class="d-flex justify-content-start h3">
                    ${empty exam.id ? 'Create New Exam' : (exam.editable ? "Edit Your Exam" : 'View Your Exam')}
            </div>
            <div class="d-flex justify-content-end">
                <a class="btn btn-outline-primary d-flex align-items-center"
                   href="/professor/exams">
                    <i class="material-icons mr-1">undo</i>Back Exams
                </a>
            </div>
        </div>

        <t:message warning="${warning}" success="${success}"></t:message>

        <div class="row justify-content-center">
            <form:form method="POST" action="/professor/exams/create" modelAttribute="exam" id="exam-form-element"
                       cssClass="w-100 needs-validation ${hasErrors ? 'was-validated' : ''}"
                       autocomplete="off" novalidate="true">

                <form:hidden path="id"/>

                <div class="card w-100">
                    <h4 class="card-header">
                        Exam Information
                    </h4>
                    <div class="card-body">
                        <div class="form-group">
                            <label class="bmd-label-floating">Title</label>
                            <form:input path="title" disabled="${exam.editable ? '' : true}" maxlength="255"
                                        cssClass="form-control" autocomplete="off" required="true"/>
                            <div class="invalid-feedback">
                                Please provide the title
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12 col-md-4">
                                <div class="form-group">
                                    <label class="bmd-label-floating">Opening time</label>
                                    <form:input path="startedAt" disabled="${exam.editable ? '' : true}"
                                                cssClass="form-control" autocomplete="off" required="true"/>
                                    <div class="invalid-feedback">
                                        Please choose the opening time
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-4">
                                <div class="form-group">
                                    <label class="bmd-label-floating">Closing time</label>
                                    <form:input path="finishedAt" disabled="${exam.editable ? '' : true}"
                                                cssClass="form-control" autocomplete="off" required="true"/>
                                    <div class="invalid-feedback">
                                        Please choose the closing time
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-4">
                                <div class="form-group">
                                    <label class="bmd-label-floating">Duration (minutes)</label>
                                    <form:input path="duration" type="number"
                                                cssClass="form-control" autocomplete="off" required="true" min="10"
                                                disabled="${exam.editable ? '' : true}"/>
                                    <div class="invalid-feedback">
                                        The duration should be greater than or equal to 10 minutes
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="bmd-label-floating">Description</label>
                            <form:textarea path="desc" disabled="${exam.editable ? '' : true}" maxlength="1000"
                                           cssClass="form-control" rows="3"/>
                        </div>
                    </div>
                </div>

                <c:forEach var="i" begin="0" end="${empty exam.questions ? 0 : fn:length(exam.questions) - 1}">
                    <t:question questionTypes="${questionTypes}"
                                editable="${exam.editable}"
                                countChoices="${empty exam.questions[i].choices ? 1 : fn:length(exam.questions[i].choices)}"
                                questionIndex="${i}"></t:question>
                </c:forEach>

                <c:if test="${exam.editable}">
                    <div class="row d-flex justify-content-center bg-light bg-faded mt-4 mb-3">

                        <button type="button" class="btn btn-primary mr-2 add-question-button">
                            Add Question
                        </button>

                        <button class="btn btn-raised btn-primary" type="submit"
                                url="/professor/exams/save">
                            Save Exam
                        </button>
                    </div>

                    <script id="question-template" type="text/x-handlebars-template">
                            <t:question questionTypes="${questionTypes}" editable="${exam.editable}"
                                        questionIndex="0" countChoices="1"></t:question>
                    </script>

                    <script id="choice-template" type="text/x-handlebars-template">
                            <t:choice questionIndex="0" choiceIndex="0" editable="${exam.editable}" questionType="0"/>
                    </script>
                </c:if>
            </form:form>
        </div>
    </jsp:body>
</t:default>