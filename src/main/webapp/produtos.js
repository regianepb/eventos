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
    $.getJSON('produtos', $('form[role=search]').serialize()).success(function (registros) {
        window.template = window.template || $('#divTable table tbody').html();
        var trHtml = window.template;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml.replace(/\{\{id\}\}/g, item.id)
                .replace(/\{\{codigo\}\}/g, item.codigo)
                .replace(/\{\{nome\}\}/g, item.nome)
                .replace(/\{\{preco\}\}/g, formatNumber(item.preco))
                .replace(/\{\{estoque\}\}/g, formatNumber(item.estoque));
        });
        $('#divTable table tbody').html(respHtml);
        $('#divForm').hide();
        $('#divTable').show();
    });
}

function salvar() {
    if (validar()) {
        var dados = $('form#produtoForm').serializeObject();
        dados.preco = parseNumber(dados.preco);
        dados.estoque = parseNumber(dados.estoque);
        $.post('produtos', dados, function (registro) {
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
    $('input[name=codigo]').closest('.form-group').removeClass('has-error');
    if (!$('input[name=codigo]').val()) {
        $('input[name=codigo]').closest('.form-group').addClass('has-error');
        showMessage('danger', 'Campo código é obrigatório');
        return false;
    }
    $('input[name=nome]').closest('.form-group').removeClass('has-error');
    if (!$('input[name=nome]').val()) {
        $('input[name=nome]').closest('.form-group').addClass('has-error');
        showMessage('danger', 'Campo nome é obrigatório');
        return false;
    }
    return true;
}

function editar(id) {
    $.getJSON("produtos?id=" + id).success(function (registro) {
        $("input[name=id]").val(registro.id);
        $("input[name=codigo]").val(registro.codigo);
        $("input[name=nome]").val(registro.nome);
        $("input[name=preco]").val(formatNumber(registro.preco));
        $("input[name=estoque]").val(formatNumber(registro.estoque));
    });
    $('#divForm').show();
    $('#divTable').hide();
}

function excluir(id) {
    if (confirm("Deseja realmente excluir o registro?")) {
        $.ajax("produtos?id=" + id, {
            type: "DELETE"
        }).success(function (registro) {
            showMessage('success', registro);
            carregar();
        });
    }
}