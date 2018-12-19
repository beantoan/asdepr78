var Common = {
    questionTypes: {
        shortAnswer: 1,
        paragraph: 2,
        multiChoice: 3,
        checkboxes: 4,
        dropdown: 5
    }
};

Common.ajaxButton = function (buttonEleSelector, successCallback, failedCallback) {
    $(buttonEleSelector).on('click', function (event) {
        event.preventDefault();

        var ele = $(this);

        $(ele).attr('disabled', true);

        var url = ele.attr('url');

        $.post(url, {}, function (data) {
            Logger.info('Professor', 'events', data);

            $.snackbar({content: data.message, timeout: 6000});

            if (data.status === 'error') {
                $(ele).attr('disabled', false);

                if (failedCallback instanceof Function) {
                    failedCallback(ele);
                }
            } else {
                if (successCallback instanceof Function) {
                    successCallback(ele, data);
                }
            }
        }).fail(function (err) {
            Logger.error('Professor', 'events', err);

            $.snackbar({
                content: 'There are some errors when processing this action. Please try again.',
                timeout: 6000
            });

            $(ele).attr('disabled', false);

            if (failedCallback instanceof Function) {
                failedCallback(ele);
            }
        });
    });
};

Common.validateForms = function () {
    var forms = document.getElementsByClassName('needs-validation');

    var validation = Array.prototype.filter.call(forms, function (form) {
        form.addEventListener('submit', function (event) {
            if (form.checkValidity() === false) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
};

Common.start = function () {
    $('body').bootstrapMaterialDesign();

    Common.validateForms();
};

$(document).ready(function () {
    Common.start();
});
