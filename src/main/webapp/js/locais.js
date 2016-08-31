$(function () {

    carregar();

    $('#btnEnviar').click(function () {
        $.post('locais', $('form').serialize(), function () {
            carregar();
            $('form').each(function () {
                this.reset();
            });
        });
    });

    $('#btnBuscar').click(function () {
        carregar();
    });
});

function carregar() {
    $.getJSON('locais', $('form[role=search]').serialize()).success(function (registros) {
        window.templateTr = window.templateTr || $('#divTable table tbody').html();
        var trHtml = window.templateTr;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{descricao\}\}/g, item.descricao);
        });
        $('#divTable table tbody').html(respHtml);
    });
}

function editar(id) {
    $.getJSON("locais?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[name=descricao]").val(data.descricao);
    });
}

function excluir(id) {
    $.ajax("locais?id=" + id, {
        type: "DELETE"
    }).success(function () {
        carregar();
    }).error(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("Não é possível excluir esse registro pois ele possui dependências.");         
    });
}


function limparForm() {
    $("input[name=id]").val("");
    $("input[name=descricao]").val("");
}

function searchKeyPress(e) {
    e = e || window.event;
    if (e.keyCode == 13)
    {
        carregar();
        return false;
    }
    return true;
}