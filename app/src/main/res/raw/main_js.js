function renderLayout(data) {
    document.getElementById('feedback-container').innerHTML = '';
    if (data && data.length) {
        for (var i = 0; i < data.length; i++) {
            var feedback = data[i];
            var domItem = renderFeedbackItem(feedback, i);
            document.getElementById('feedback-container').appendChild(domItem);
        }
    } else {
        document.getElementById('feedback-container').innerHTML =
            document.getElementById('nofeedback-template').innerHTML;
    }
}

function renderFeedbackItem(feedback, i) {
    var container = document.createElement('div');
    container.innerHTML = document.getElementById('feedback-template').innerHTML;
    container.querySelector('.index').innerHTML = i;
    container.querySelector('.title').innerHTML = feedback.feedbackName;
    container.querySelector('.date').innerHTML = feedback.startDate;
    container.querySelector('.btn').addEventListener('click', function (e) {
        if (typeof LF !== 'undefined') {
            LF.doFeedback(feedback.id);
        }
    });
    return container.firstChild;
}

function pendingLoad() {
    ajax({
        url: '/api/pending-feedback',
        method: 'GET',
        success: function (data) {
            window.feedbacks = data;
            renderLayout(data);
        },
        fail: function () {
            window.feedbacks = [];
            renderLayout([]);
        }
    })
}

function ajax(option) {
    if (option) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4) {
                if (this.status === 200 && option.success) {
                    var data = JSON.parse(this.responseText);
                    option.success(data);
                } else if (this.status !== 200 && option.fail) {
                    option.fail(this);
                }
            }
        };
        xhttp.open(option.method, option.url, true);
        xhttp.send();
    }
}