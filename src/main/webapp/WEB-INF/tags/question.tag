<%@attribute name="editable" required="true" type="java.lang.Boolean" %>
<%@attribute name="questionTypes" required="true" type="java.util.Map" %>
<%@attribute name="countChoices" required="true" type="java.lang.Integer" %>
<%@attribute name="questionIndex" required="true" type="java.lang.Integer" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="questionType" value="${exam.questions[questionIndex].type}"/>

<div id="questions${questionIndex}" index="${questionIndex}" class="card w-100 mt-3 question-element">

    <form:hidden path="questions[${questionIndex}].id" cssClass="id-hidden-element input-element"/>

    <h4 class="card-header">
        <div class="d-flex justify-content-between">
            <div class="d-flex justify-content-start align-items-center">
                <i class="material-icons mr-2">help_outline</i>
                <div>Question&nbsp;<span class="question-index">${questionIndex + 1}</span></div>
            </div>
            <div class="d-flex justify-content-end">
                <c:if test="${editable}">
                    <button type="button" question-id="#questions${questionIndex}"
                            class="btn btn-primary text-danger remove-question-button ${questionIndex eq 0 ? 'd-none' : ''}">
                        Remove
                    </button>
                </c:if>
            </div>
        </div>
    </h4>
    <div class="card-body">

        <div class="row">
            <div class="col-md-12 col-lg-9">
                <div class="form-group">
                    <label class="bmd-label-floating">Title</label>
                    <form:input path="questions[${questionIndex}].title"
                                disabled="${editable ? '' : true}" required="true" maxlength="150"
                                cssClass="form-control input-element" autocomplete="off"/>
                    <div class="invalid-feedback">
                        Please provide the title
                    </div>
                </div>
            </div>
            <div class="col-6 col-lg-1">
                <div class="form-group">
                    <label class="bmd-label-floating">Point</label>
                    <form:input path="questions[${questionIndex}].point" type="number" min="0" required="true"
                                disabled="${editable ? '' : true}"
                                cssClass="form-control input-element" autocomplete="off"/>
                    <div class="invalid-feedback">
                        Please provide the point
                    </div>
                </div>
            </div>
            <div class="col-6 col-lg-2 form-group">
                <div class="checkbox">
                    <label>
                        <form:checkbox path="questions[${questionIndex}].required"
                                       cssClass="input-element question-required-element"
                                       disabled="${editable ? '' : true}"/>
                        Mandatory
                    </label>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12 col-lg-9">
                <div class="form-group">
                    <label class="bmd-label-floating">Description</label>
                    <form:textarea path="questions[${questionIndex}].desc"
                                   disabled="${editable ? '' : true}" maxlength="255"
                                   cssClass="form-control input-element" rows="2"/>
                </div>
            </div>
            <div class="col-md-12 col-lg-3">
                <div class="form-group">
                    <form:select path="questions[${questionIndex}].type" multiple="false" required="true"
                                 cssClass="form-control question-type-element input-element"
                                 disabled="${editable ? '' : true}"
                                 question-id="#questions${questionIndex}"
                                 items="${questionTypes}">
                    </form:select>
                    <div class="invalid-feedback">
                        Please choose a type
                    </div>
                </div>
            </div>
        </div>

        <div class="solution-element ${questionType eq 3 or questionType eq 4 or questionType eq 5 ? 'd-none' : ''}">
            <div class="form-group">
                <label class="bmd-label-floating">Solution</label>
                <form:textarea path="questions[${questionIndex}].textSolution"
                               disabled="${editable ? '' : true}" maxlength="255"
                               cssClass="form-control input-element" rows="2"/>
            </div>
        </div>

        <div class="options-element ${empty questionType or questionType eq 1 or questionType eq 2 ? 'd-none' : ''}">
            <c:forEach var="i" begin="0" end="${countChoices-1}">
                <t:choice questionIndex="${questionIndex}" choiceIndex="${i}" editable="${editable}"/>
            </c:forEach>

            <div class="row mt-4 mr-1 d-flex justify-content-end">
                <c:if test="${editable}">
                    <button type="button" class="btn btn-primary add-choice-button"
                            question-id="#questions${questionIndex}">
                        Add Option
                    </button>
                </c:if>
            </div>
        </div>
    </div>
</div>