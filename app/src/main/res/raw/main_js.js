var conductResult = [];
var conductTracking = {};
var conductTotal = 0;

function renderLayout(data) {
    document.getElementById('feedback-container').innerHTML = '';
    if (data && data.length) {
        for (var i = 0; i < data.length; i++) {
            var feedback = data[i].feedbackByFeedbackId;
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

function updateProcess(qId, option) {
    return function (e) {
        switch (e.target.type) {
            case 'checkbox':
                if (!conductTracking[qId]) {
                    conductTracking[qId] = {};
                }
                //conductTracking[qId][option.id] = e.target.checked;
                break;
            case 'radio':
                conductTracking[qId] = option.id;
                break;
            default:
                conductTracking[qId] = {};
                conductTracking[qId][option.id] = e.target.value;
                break;
        }
        var process = Object.keys(conductTracking).length;
        process = process * 100 / conductTotal;
        document.querySelector('.progress-bar').style.width = process + '%';
        document.querySelector('.progress-bar span').innerHTML = process + '%';
    }
}

function renderChoiseTemplate(template, data, index) {
    template.querySelector('h2').innerHTML = 'Câu hỏi ' + index;
    template.querySelector('h4').innerHTML = data.questionContent;
    var liTemplate = template.querySelector('li').outerHTML;
    template.querySelector('li').remove();
    var cTemp = document.createElement('div');
    for (var i = 0; i < data.optionsById.length; i++) {
        var option = data.optionsById[i];
        cTemp.innerHTML = liTemplate;
        cTemp.querySelector('input').name = 'q' + data.id;
        cTemp.querySelector('input').value = option.point;
        if (option.point <= 0) {
            cTemp.querySelector('span').remove();
            cTemp.querySelector('input[type=text]').name = 'q' + data.id + '-other';
        } else {
            cTemp.querySelector('input[type=text]').remove();
            cTemp.querySelector('span').innerHTML = option.optionnContent;
        }
        cTemp.querySelector('input')
            .addEventListener('change', updateProcess('q' + data.id, option));
        template.querySelector('ul')
            .appendChild(cTemp.querySelector('li'));
    }
    return template;
}

function renderStarTemplate(template, data, index) {
    template.querySelector('h2').innerHTML = 'Câu hỏi ' + index;
    template.querySelector('h4').innerHTML = data.questionContent;
    var liTemplate = template.querySelector('li').outerHTML;
    template.querySelector('li').remove();
    var cTemp = document.createElement('div');
    for (var i = 0; i < data.optionsById.length; i++) {
        var option = data.optionsById[i];
        cTemp.innerHTML = liTemplate;
        cTemp.querySelector('input').name = 'q' + data.id;
        cTemp.querySelector('input').value = option.point;
        var arr = [];
        arr[option.point] = "";
        cTemp.querySelector('input[type=text]').remove();
        cTemp.querySelector('span').innerHTML = arr.join("★");
        cTemp.querySelector('input')
            .addEventListener('change', updateProcess('q' + data.id, option));
        template.querySelector('ul')
            .appendChild(cTemp.querySelector('li'));
    }
    return template;
}

function renderLongtextTemplate(template, data, index) {
    template.querySelector('h2').innerHTML = 'Câu hỏi ' + index;
    template.querySelector('h4').innerHTML = data.questionContent;
    template.querySelector('textarea').name = 'q' + data.id;
    template.querySelector('textarea')
        .addEventListener('change', updateProcess('q' + data.id, data.optionsById[0]));
    return template;
}

function renderTextTemplate(template, data, index) {
    template.querySelector('h2').innerHTML = 'Câu hỏi ' + index;
    template.querySelector('h4').innerHTML = data.questionContent;
    template.querySelector('input').name = 'q' + data.id;
    template.querySelector('input')
        .addEventListener('change', updateProcess('q' + data.id, data.optionsById[0]));
    return template;
}

function renderFeedback(data) {
    document.getElementById('feedback-container').innerHTML = '';
    if (data) {
        conductTotal = data.questionsById.length;
        for (var i = 0; i < data.questionsById.length; i++) {
            var question = data.questionsById[i];
            var id = '';
            var renderFn;
            switch (question.type) {
                case 'CheckBox':
                    id = 'checkbox-question';
                    renderFn = renderChoiseTemplate;
                    break;
                case 'Radio':
                    id = 'radio-question';
                    renderFn = renderChoiseTemplate;
                    break;
                case 'Star':
                    id = 'radio-question';
                    renderFn = renderStarTemplate;
                    break;
                case 'TextArea':
                    id = 'longtext-question';
                    renderFn = renderLongtextTemplate;
                    break;
                case 'Text':
                    id = 'text-question';
                    renderFn = renderTextTemplate;
                    break;
            }
            if (id) {
                template = document.getElementById(id).innerHTML;
                var cTemp = document.createElement('div');
                cTemp.innerHTML = template;
                template = cTemp.querySelector('.card');
                var qNode = renderFn(template, question, i + 1);
                document.getElementById('feedback-container').appendChild(qNode);
            }
        }
    }
}

function showLoginMessage(msg){
    document.getElementById('error').innerHTML = msg;
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

function feedbackLoad() {
    conductResult = [];
    ajax({
        url: '/api/detail-feedback',
        method: 'GET',
        success: function (data) {
            window.feedback = data;
            renderFeedback(data);
        },
        fail: function () {
            window.feedback = null;
            renderFeedback();
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

if (document.getElementById('btn-login')) {
    document.getElementById('btn-login')
        .addEventListener('click', function () {
            var username = document.getElementById('username').value;
            var password = document.getElementById('password').value;
            var login  = {
                username:username,
                password:password
            };
            if (typeof LG !== 'undefined') {
                LG.login(JSON.stringify(login));
            }
        });
}
if (document.getElementById('btn-send-result')) {

    document.getElementById('btn-send-result')
        .addEventListener('click', function () {
            conductResult = [];
            for (var i in conductTracking) {
                if (typeof conductTracking[i] === 'object') {
                    for (var j in conductTracking[i]) {
                        if (conductTracking[i][j]) {
                            conductResult.push({
                                optionnByOptionnId: j,
                                answerContent: conductTracking[i][j]
                            });
                        }
                        /* if (conductTracking[i][j] != null) {
                             conductResult.push({
                                 optionnByOptionnId: j,
                                 answerContent: conductTracking[i][j]
                             });
                             console.log(j);
                             console.log(conductTracking[i][j]);
                             } else {
                                 conductResult.push({
                                     optionnByOptionnId: j
                                 });
                                  console.log(j);
                             }
                         }*/
                    }
                } else {
                    conductResult.push({
                        optionnByOptionnId: conductTracking[i]
                        // answerContent: ''
                    });
                    console.log(conductTracking[i]);
                }
            }
            var content = {
                "feedbackId": window.feedback.id,
                "answers": conductResult
            };
            if (typeof FD !== 'undefined') {
                FD.save(JSON.stringify(content));
            }
            console.log(content);

        });
}