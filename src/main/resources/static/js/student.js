var Student = {};

Student.countdown = function () {
    var hours, minutes, seconds;

    var duration = parseInt($('#exam-duration-value').text());

    var endDate = new Date((new Date()).getTime() + 60000 * duration).getTime();

    if (isNaN(endDate)) {
        $('#exam-duration').text("00:00:00");
        return;
    }

    var interval = setInterval(calculate, 1000);

    function calculate() {
        var startDate = new Date();
        startDate = startDate.getTime();

        var timeRemaining = parseInt((endDate - startDate) / 1000);

        if (timeRemaining >= 0) {
            hours = parseInt(timeRemaining / 3600);
            timeRemaining = (timeRemaining % 3600);

            minutes = parseInt(timeRemaining / 60);
            timeRemaining = (timeRemaining % 60);

            seconds = parseInt(timeRemaining);

            var timeRemainingStr = ("0" + hours).slice(-2) + ":" + ("0" + minutes).slice(-2) + ":" + ("0" + seconds).slice(-2);
            $('#exam-duration').text(timeRemainingStr);
        } else {
            $('#exam-duration').text("00:00:00");
            clearInterval(interval);
            return;
        }
    }
};

Student.start = function () {
    Student.countdown();
};

$(document).ready(function () {
    Student.start();
});