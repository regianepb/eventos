$(function () {

    carregar();
    carregarClassifDespesas();

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
        carregarClassifDespesas();
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
                    .replace(/\{\{classif_despesas_id\}\}/g, item.classif_despesas_id.descricao);
        });
        $('#divTable table tbody').html(respHtml);
    });
}

function carregarClassifDespesas(executa) {
    $.getJSON('classifdespesas').success(function (classifdespesas) {
        var options = "";
        classifdespesas.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.descricao + '</option>';
        });
        $('#despesasForm select[name=classif_despesas_id]').html(options);
        if (executa)
            executa();
    });
}


function editar(id) {
    $.getJSON("despesas?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[name=descricao]").val(data.descricao);        
        $("select[name=classif_despesas_id]").val(data.classif_despesas_id.id);
    });
}

function excluir(id) {
    $.ajax("despesas?id=" + id, {
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
    $("select[name=classif_despesas_id]").val("");
    carregarClassifDespesas();
}
