$(function () {

    carregar();

    $('#btnEnviar').click(function () {
        $.post('recursos', $('form').serialize(), function () {
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
    $.getJSON('recursos', $('form[role=search]').serialize()).success(function (registros) {
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
    $.getJSON("recursos?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[name=descricao]").val(data.descricao);
    });
}

function excluir(id) {
    $.ajax("recursos?id=" + id, {
        type: "DELETE"
    }).success(function () {
        carregar();
    }).error(function () {
        alert("Não é possível excluir esse registro pois ele possui dependências.");         
    });
}

function limparForm() {
    $("input[name=id]").val("");
    $("input[name=descricao]").val("");
}
