$(function(){
    // carregar o menu
    $.get('menu.html', function (data) {
        $("div#header").html(data);
    });
});

function showMessage(type, message) {
    $('#message').removeClass('alert-success').removeClass('alert-info')
        .removeClass('alert-warning').removeClass('alert-danger')
        .addClass('alert-' + type).html('<p>'+message+'</p>').show();
}

function hideMessage() {
    $('#message').hide();
}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function formatNumber(value) {
    if (value)
        return new Number(value).toLocaleString('pt-BR', {minimumFractionDigits: 2});
    return "";
}

function formatDate(value) {
    if (value)
        return new Date(value).toLocaleString('pt-BR');
    return "";
}

function parseNumber(strNum) {
    strNum = onlyNumeric(strNum);
    strNum = strNum.replace(/\./g, 'g').replace(/,/g, 'v').replace(/v/, '.').replace(/g/,'');
    return strNum;
}

function onlyNumeric(str) {
    var strNum = "";
    if (!str) return str;
    for (var i = 0; i < str.length; i++) {
        if ((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == '.' || str.charAt(',')) {
            strNum += str.charAt(i);
        } else {
            return strNum;
        }
    }
    return strNum;
}