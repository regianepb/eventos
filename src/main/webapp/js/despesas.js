$(function () {

    carregar();

    $('#btnEnviar').click(function () {
        $.post('despesas', $('form').serialize(), function () {
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
    $.getJSON('despesas', $('form[role=search]').serialize()).success(function (registros) {
        window.templateTr = window.templateTr || $('#divTable table tbody').html();
        var trHtml = window.templateTr;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{descricao\}\}/g, item.descricao)
                    .replace(/\{\{classif_despesas_id\}\}/g, item.classif_despesas_id);
        });
        $('#divTable table tbody').html(respHtml);
    });
}

function editar(id) {
    $.getJSON("despesas?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[name=descricao]").val(data.descricao);
        $("input[name=classif_despesas_id]").val(data.classif_despesas_id);
    });
}

function excluir(id) {
    $.ajax("despesas?id=" + id, {
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
    $("input[name=classif_despesas_id]").val("");
}
