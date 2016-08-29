$(function () {
    // Preencher table
    carregar();
    // Ação de salvar o formulário
    $('#btnSalvar').click(function () {
        salvar();
    });
    $('#btnNovo').click(function () {
        $('#divForm').show();
        $('#divTable').hide();
    });
    $('#btnCancelar').click(function () {
        $('#divForm').hide();
        $('#divTable').show();
    });
    $('#btnBuscar').click(function () {
        carregar();
    });
});

function carregar() {
    $.getJSON('clientes', $('form[role=search]').serialize()).success(function (registros) {
        window.template = window.template || $('#divTable table tbody').html();
        var trHtml = window.template;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml.replace(/\{\{id\}\}/g, item.id)
                .replace(/\{\{codigo\}\}/g, item.codigo)
                .replace(/\{\{nome\}\}/g, item.nome)
                .replace(/\{\{telefone\}\}/g, item.telefone)
                .replace(/\{\{documento\}\}/g, item.documento)
                .replace(/\{\{email\}\}/g, item.email);
        });
        $('#divTable table tbody').html(respHtml);
        $('#divForm').hide();
        $('#divTable').show();
    });
}

function salvar() {
    if (validar()) {
        $.post('clientes', $('form').serialize(), function (registro) {
            showMessage('success', registro.nome + ' salvo com sucesso!!!');
            $('form').each(function() {
                this.reset();
            });
            carregar();
        });
    }
}

function validar() {
    hideMessage();
    $('input[name=nome]').closest('.form-group').removeClass('has-error');
    if (!$('input[name=nome]').val()) {
        $('input[name=nome]').closest('.form-group').addClass('has-error');
        showMessage('danger', 'Campo nome é obrigatório');
        return false;
    }
    return true;
}

function editar(id) {
    $.getJSON("clientes?id=" + id).success(function (registro) {
        $("input[name=id]").val(registro.id);
        $("input[name=codigo]").val(registro.codigo);
        $("input[name=nome]").val(registro.nome);
        $("input[name=documento]").val(registro.documento);
        $("input[name=telefone]").val(registro.telefone);
        $("input[name=email]").val(registro.email);
    });
    $('#divForm').show();
    $('#divTable').hide();
}

function excluir(id) {
    if (confirm("Deseja realmente excluir o registro?")) {
        $.ajax("clientes?id=" + id, {
            type: "DELETE"
        }).success(function (registro) {
            showMessage('success', registro);
            carregar();
        });
    }
}