$(function () {
    // Preencher table
    showListPedidos();
    // Ação de salvar o formulário
    $('#formPedido #btnSalvar').click(function () {
        salvarPedido();
    });
    $('#listPedidos #btnNovo').click(function () {
        showFormPedido();
        hideMessage();
    });
    $('#formPedido #btnCancelar').click(function () {
        showListPedidos();
        hideMessage();
    });
    $('#listItens #btnVoltarItens').click(function () {
        showListPedidos();
        hideMessage();
    });
    $('#formPedido #btnItens').click(function () {
        showListItens($("#pedidoForm input[name=id]").val());
        hideMessage();
    });
    $('#listPedidos #btnBuscar').click(function () {
        carregarPedidos();
    });
    $('#listItens #btnNovoItem').click(function () {
        showFormItem();
    });
    $("#itemModal #btnSalvarItem").click(function(){
        salvarItem();
    });
});

function carregarPedidos() {
    $.getJSON('pedidos', $('#listPedidos form[role=search]').serialize()).success(function (registros) {
        window.template = window.template || $('#listPedidos table tbody').html();
        var trHtml = window.template;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml.replace(/\{\{id\}\}/g, item.id)
                .replace(/\{\{numero\}\}/g, item.numero)
                .replace(/\{\{cliente\.nome\}\}/g, item.cliente.nome)
                .replace(/\{\{emissao\}\}/g, formatDate(item.emissao))
                .replace(/\{\{aprovacao\}\}/g, formatDate(item.aprovacao) || geraLinkAprovacao(item.id))
                .replace(/\{\{valorTotal\}\}/g, formatNumber(item.valorTotal));
        });
        $('#listPedidos table tbody').html(respHtml);
    });
}

function geraLinkAprovacao(id) {
    return '<a href="#" onclick="aprovarPedido(' + id + ')"><span class="glyphicon glyphicon-ok"/> Aprovar</a>';
}

function carregarItens(idPedido) {
    $.getJSON('pedidos/itens', 'idPedido=' + idPedido).success(function (registros) {
        window.templateItem = window.templateItem || $('#listItens table tbody').html();
        var trHtml = window.templateItem;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml.replace(/\{\{id\}\}/g, item.id)
                .replace(/\{\{produto\.nome\}\}/g, item.produto.nome)
                .replace(/\{\{quantidade\}\}/g, formatNumber(item.quantidade))
                .replace(/\{\{valorUnitario\}\}/g, formatNumber(item.valorUnitario))
                .replace(/\{\{valorTotal\}\}/g, formatNumber(item.valorTotal));
        });
        $('#listItens table tbody').html(respHtml);
    });
}

function salvarPedido() {
    if (validarPedido()) {
        var dados = $('form#pedidoForm').serializeObject();
        dados.valorTotal = parseNumber(dados.valorTotal);
        dados.desconto = parseNumber(dados.desconto);
        $.post('pedidos', dados, function (registro) {
            showMessage('success', registro.numero + ' salvo com sucesso!!!');
            showFormPedido(registro);
        });
    }
}

function aprovarPedido(id) {
    $.getJSON("pedidos?id=" + id).success(function (registro) {
        registro.aprovacao = formatDate(new Date());
        registro.emissao = formatDate(registro.emissao);
        registro.cliente = registro.cliente.id;
        $.post('pedidos', registro, function(registro) {
            showListPedidos();
            showMessage('success', registro.numero + ' aprovado com sucesso!!!');
        });
    });
}

function salvarItem() {
    if (validarItem()) {
        var idPedido = $("#pedidoForm input[name=id]").val();
        var dados = $('form#itemForm').serializeObject();
        dados.valorUnitario = parseNumber(dados.valorUnitario);
        dados.valorTotal = parseNumber(dados.valorTotal);
        dados.quantidade = parseNumber(dados.quantidade);
        dados.idPedido = idPedido;
        $.post('pedidos/itens', dados, function (registro) {
            showListItens(idPedido);
            $('#itemModal').modal("hide");
            $('#itemModal form').each(function(){
                this.reset();
            });
        });
    }
}

function carregarClientes(executa) {
    $.getJSON('clientes').success(function (clientes) {
        var options = "";
        clientes.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.nome + '</option>';
        });
        $('#pedidoForm select[name=cliente]').html(options);
        if (executa) executa();
    });
}

function carregarProdutos(executa) {
    $.getJSON('produtos').success(function (produtos) {
        var options = "";
        produtos.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.nome + ' (' + item.codigo + ')</option>';
        });
        $('#itemForm select[name=produto]').html(options);
        if (executa) executa();
    });
}

function validarItem() {
    $('#itemForm select[name=produto]').closest('.form-group').removeClass('has-error');
    if (!$('#itemForm select[name=produto]').val()) {
        $('#itemForm select[name=produto]').closest('.form-group').addClass('has-error');
        return false;
    }
    $('#itemForm input[name=quantidade]').closest('.form-group').removeClass('has-error');
    if (!parseNumber($('#itemForm input[name=quantidade]').val())) {
        $('#itemForm input[name=quantidade]').closest('.form-group').addClass('has-error');
        return false;
    }
    $('#itemForm input[name=valorUnitario]').closest('.form-group').removeClass('has-error');
    if (!parseNumber($('#itemForm input[name=valorUnitario]').val())) {
        $('#itemForm input[name=valorUnitario]').closest('.form-group').addClass('has-error');
        return false;
    }
    return true;
}

function validarPedido() {
    hideMessage();
    $('#pedidoForm select[name=cliente]').closest('.form-group').removeClass('has-error');
    if (!$('#pedidoForm select[name=cliente]').val()) {
        $('#pedidoForm select[name=cliente]').closest('.form-group').addClass('has-error');
        showMessage('danger', 'Campo cliente é obrigatório');
        return false;
    }
    return true;
}

function editarPedido(id) {
    $.getJSON("pedidos?id=" + id).success(function (registro) {
        showFormPedido(registro);
    });
}

function excluirPedido(id) {
    if (confirm("Deseja realmente excluir o registro?")) {
        $.ajax("pedidos?id=" + id, {
            type: "DELETE"
        }).success(function (registro) {
            showMessage('success', registro);
            carregarPedidos();
        });
    }
}

function editarItem(id) {
    $.getJSON("pedidos/itens?id=" + id).success(function (registro) {
        showFormItem(registro);
    });
}

function excluirItem(id) {
    if (confirm("Deseja realmente excluir o registro?")) {
        $.ajax("pedidos/itens?id=" + id, {
            type: "DELETE"
        }).success(function (registro) {
            showMessage('success', registro);
            carregarItens($("#pedidoForm input[name=id]").val());
        });
    }
}

function showFormItem(item) {
    carregarProdutos(function(){
        if (item) {
            $("#itemForm input[name=id]").val(item.id);
            $("#itemForm select[name=produto]").val(item.produto.id);
            $("#itemForm input[name=quantidade]").val(formatNumber(item.quantidade));
            $("#itemForm input[name=valorUnitario]").val(formatNumber(item.valorUnitario));
            $("#itemForm input[name=valorTotal]").val(formatNumber(item.valorTotal));
        }
        $('#itemModal').modal("show");
    });
}

function showFormPedido(registro) {
    $('div.separa').hide();
    $('#formPedido').show();
    carregarClientes(function () {
        if (registro) {
            $("#pedidoForm input[name=id]").val(registro.id);
            $("#pedidoForm input[name=numero]").val(registro.numero);
            $("#pedidoForm input[name=emissao]").val(formatDate(registro.emissao));
            $("#pedidoForm select[name=cliente]").val(registro.cliente.id);
            $("#pedidoForm input[name=formaPagto]").val(registro.formaPagto);
            $("#pedidoForm input[name=condicaoPagto]").val(registro.condicaoPagto);
            $("#pedidoForm input[name=desconto]").val(formatNumber(registro.desconto));
            $("#pedidoForm input[name=valorTotal]").val(formatNumber(registro.valorTotal));
            $("#pedidoForm #btnItens").removeAttr('disabled');
        } else {
            $("#pedidoForm #btnItens").attr('disabled', true);
        }
    });
}

function showListItens(idPedido) {
    carregarItens(idPedido);
    $('div.separa').hide();
    $('#listItens').show();
}

function showListPedidos() {
    carregarPedidos();
    $('div.separa').hide();
    $('#listPedidos').show();
}