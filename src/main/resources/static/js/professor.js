var Professor = {};

Professor.reviewSubmission = function () {
    Common.ajaxButton('.button-answer-as-correct', function (ele, data) {
        $(ele).closest('.submission-card').find('.card-header').addClass('bg-primary');
        $(ele).parent().remove();
    });

    Common.ajaxButton('.button-answer-as-incorrect', function (ele, data) {
        $(ele).closest('.submission-card').find('.card-header').addClass('bg-warning');
        $(ele).parent().remove();
    });

    Common.ajaxButton('.button-submission-as-reviewed', function (ele, data) {
        $(ele).parent().remove();
    });
};

Professor.addQuestion = function () {
    var lastQuestionEle = $('#exam-form-element .question-element:last');
    var newQuestionIndex = parseInt(lastQuestionEle.attr('index')) + 1;
    var newQuestionEleId = '#questions' + newQuestionIndex;

    Logger.info('Professor', 'addQuestion', 'newQuestionIndex=' + newQuestionIndex, 'questionEleId=' + newQuestionEleId);

    var html = $('#question-template').html();

    html = html.replace(/questions0/g, 'questions' + newQuestionIndex);

    lastQuestionEle.after(html);

    var newQuestionEle = $(newQuestionEleId);
    var countQuestions = $('#exam-form-element .question-element').length;

    newQuestionEle.attr('index', newQuestionIndex);
    newQuestionEle.find('.question-index').text(countQuestions);
    newQuestionEle.find('.remove-question-button').removeClass('d-none');
    newQuestionEle.find('.id-hidden-element').remove();

    Professor.updateQuestionIndexes();

    newQuestionEle.on('click', '.remove-question-button', function (event) {
        event.preventDefault();

        Professor.removeQuestion($(this).attr('question-id'));
    });

    newQuestionEle.on('click', '.add-choice-button', function (event) {
        event.preventDefault();

        Professor.addChoice($(this).attr('question-id'));
    });

    newQuestionEle.on('change', '.question-type-element', function (event) {
        var ele = $(this);

        Professor.onQuestionTypeChanged(ele.attr('question-id'), ele.val());
    });

    Professor.resetInputs(newQuestionEle);

    newQuestionEle.bootstrapMaterialDesign();
};

Professor.updateQuestionIndexes = function () {
    $('#exam-form-element .question-element').each(function (questionIndex, questionEle) {
        var $questionEle = $(questionEle);

        $questionEle.find('.question-index').text(questionIndex + 1);

        $questionEle.find('.input-element,input:hidden').each(function (index, ele) {
            var $ele = $(ele);
            var newName = $ele.attr('name').replace(/questions\[\d+\]/, 'questions[' + questionIndex + ']');

            $ele.attr('name', newName);
        });
    });
};

Professor.removeQuestion = function (questionEleId) {
    Logger.info('Professor', 'removeQuestion', 'questionEleId=' + questionEleId);

    var questionEle = $(questionEleId);

    questionEle.remove();

    Professor.updateQuestionIndexes();
};

Professor.resetInputs = function (newEle) {
    newEle.find('.input-element[type=text],.input-element[type=number],textarea.input-element').each(function (index, ele) {
        $(ele).val('');
    });

    newEle.find('.question-required-element').each(function (index, ele) {
        $(ele).prop("checked", true).change();
    });

    newEle.find('.choice-correct-element').each(function (index, ele) {
        $(ele).prop("checked", false).change();
    });

    newEle.find('select.input-element').each(function (index, ele) {
        $(ele).find('option:eq(0)').prop('selected', true).change();
    });
};

Professor.addChoice = function (questionEleId) {
    var questionEle = $(questionEleId);

    var questionIndex = questionEle.attr('index');
    var lastChoiceEle = questionEle.find('.choice-element:last');
    var newChoiceIndex = parseInt(lastChoiceEle.attr('index')) + 1;
    var newChoiceEleId = '#questions' + questionIndex + '-choices' + newChoiceIndex;

    Logger.info('Professor', 'addChoice', 'questionEleId=' + questionEleId, 'newChoiceIndex=' + newChoiceIndex,
        'newChoiceEleId=' + newChoiceEleId);

    var html = $('#choice-template').html();

    html = html.replace(/questions\[0\]/g, 'questions[' + questionIndex + ']');
    html = html.replace(/questions0/g, 'questions' + questionIndex);
    html = html.replace(/choices0/g, 'choices' + newChoiceIndex);

    lastChoiceEle.after(html);

    var newChoiceEle = $(newChoiceEleId);
    var countChoices = questionEle.find('.choice-element').length;

    newChoiceEle.attr('index', newChoiceIndex);
    newChoiceEle.find('.choice-index').text(countChoices);
    newChoiceEle.find('.remove-choice-button').removeClass('invisible');
    newChoiceEle.find('.id-hidden-element').remove();

    Professor.updateChoiceIndexes(questionEleId);

    newChoiceEle.on('click', '.remove-choice-button', function (event) {
        event.preventDefault();

        var ele = $(this);

        Professor.removeChoice(ele.attr('question-id'), ele.attr('choice-id'));
    });

    Professor.resetInputs(newChoiceEle);

    newChoiceEle.bootstrapMaterialDesign();
};

Professor.removeChoice = function (questionEleId, choiceEleId) {
    Logger.info('Professor', 'removeChoice', 'questionEleId=' + questionEleId, 'choiceEleId=' + choiceEleId);

    var choiceEle = $(choiceEleId);

    choiceEle.remove();

    Professor.updateChoiceIndexes(questionEleId);
};

Professor.updateChoiceIndexes = function (questionEleId) {
    Logger.info('Professor', 'updateChoiceIndexes', 'questionEleId=' + questionEleId);

    var questionEle = $(questionEleId);

    questionEle.find('.choice-element').each(function (choiceIndex, choiceEle) {
        var $choiceEle = $(choiceEle);

        $choiceEle.find('.choice-index').text(choiceIndex + 1);

        $choiceEle.find('.input-element,input:hidden').each(function (index, ele) {
            var $ele = $(ele);
            var newName = $ele.attr('name').replace(/choices\[\d+\]/, 'choices[' + choiceIndex + ']');

            $ele.attr('name', newName);
        });
    });
};

Professor.onQuestionTypeChanged = function (questionEleId, questionTypeVal) {
    Logger.info('Professor', 'onQuestionTypeChanged', 'questionEleId=' + questionEleId,
        'questionTypeVal=' + questionTypeVal);

    var optionsEle = $(questionEleId + ' .options-element');
    var solutionEle = $(questionEleId + ' .solution-element');

    switch (parseInt(questionTypeVal)) {
        case Common.questionTypes.shortAnswer:
        case Common.questionTypes.paragraph:
            optionsEle.addClass('d-none');
            solutionEle.removeClass('d-none');
            break;
        case Common.questionTypes.checkboxes:
        case Common.questionTypes.multiChoice:
        case Common.questionTypes.dropdown:
            optionsEle.removeClass('d-none');
            solutionEle.addClass('d-none');
            break;
    }
};

Professor.examFormEvents = function () {
    $('.add-question-button').on('click', function (event) {
        event.preventDefault();

        Professor.addQuestion();
    });

    $('.remove-question-button').on('click', function (event) {
        event.preventDefault();

        Professor.removeQuestion($(this).attr('question-id'));
    });

    $('.add-choice-button').on('click', function (event) {
        event.preventDefault();

        Professor.addChoice($(this).attr('question-id'));
    });

    $('.remove-choice-button').on('click', function (event) {
        event.preventDefault();

        var ele = $(this);

        Professor.removeChoice(ele.attr('question-id'), ele.attr('choice-id'));
    });

    $('.question-type-element').on('change', function (event) {
        var ele = $(this);

        Professor.onQuestionTypeChanged(ele.attr('question-id'), ele.val());
    });
};

Professor.examDatetimePicker = function () {
    var dateTimeConfig = {
        weekStart: 0,
        format: 'YYYY-MM-DD HH:mm:00',
        minDate: new Date()
    };

    $('#finishedAt').bootstrapMaterialDatePicker(dateTimeConfig);

    $('#startedAt').bootstrapMaterialDatePicker(dateTimeConfig).on('change', function (e, date) {
        $('#finishedAt').bootstrapMaterialDatePicker('setMinDate', date);
    });
};

Professor.createExam = function () {

    Professor.examFormEvents();

    Professor.examDatetimePicker();
};