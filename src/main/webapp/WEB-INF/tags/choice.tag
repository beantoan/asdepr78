<%@ attribute name="questionType" required="true" type="java.lang.Integer" %>
<%@ attribute name="editable" required="true" type="java.lang.Boolean" %>
<%@ attribute name="questionIndex" required="true" type="java.lang.Integer" %>
<%@ attribute name="choiceIndex" required="false" type="java.lang.Integer" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="questions${questionIndex}-choices${choiceIndex}" index="${choiceIndex}"
     class="bmd-form-group input-group choice-element">

    <form:hidden path="questions[${questionIndex}].choices[${choiceIndex}].id"
                 cssClass="id-hidden-element input-element"/>

    <div class="input-group-prepend mr-1">
        <span class="input-group-text">
            <i class="material-icons choice-icon choice-icon-3 mr-1"
               style="${questionType eq 3 ? '' : 'display: none'}">radio_button_unchecked</i>
            <i class="material-icons choice-icon choice-icon-4 mr-1"
               style="${questionType eq 4 ? '' : 'display: none'}">check_box_outline_blank</i>

            #<span class="choice-index">${choiceIndex + 1}</span></span>
    </div>

    <form:input path="questions[${questionIndex}].choices[${choiceIndex}].title"
                disabled="${exam.editable ? '' : true}" maxlength="255"
                cssClass="form-control input-element mr-2" autocomplete="off"/>

    <div class="invalid-feedback">
        Please provide the title
    </div>

    <div class="input-group-append">
        <div class="checkbox mr-1">
            <label>
                <form:checkbox path="questions[${questionIndex}].choices[${choiceIndex}].correct"
                               cssClass="input-element choice-correct-element"
                               disabled="${exam.editable ? '' : true}"/>
                Correct
            </label>
        </div>

        <c:if test="${editable}">
            <button type="button" aria-label="Close"
                    class="input-group-text close remove-choice-button ${choiceIndex eq 0 ? 'invisible' : 'visible'}"
                    question-id="#questions${questionIndex}"
                    choice-id="#questions${questionIndex}-choices${choiceIndex}">
                <i class="material-icons">close</i>
            </button>
        </c:if>
    </div>
</div>