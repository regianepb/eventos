$(function () {

    carregar();

    $('#btnEnviar').click(function () {
        $.post('classifdespesas', $('form').serialize(), function () {
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
    $.getJSON('classifdespesas', $('form[role=search]').serialize()).success(function (registros) {
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
    $.getJSON("classifdespesas?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[descricao=descricao]").val(data.descricao);
    });
}

function excluir(id) {
    $.ajax("classifdespesas?id=" + id, {
        type: "DELETE"
    }).success(function () {
        carregar();
    }
    );
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