$(function () {        
    carregar();
    carregarLocais(); 

    $('#btnEnviar').click(function () {
        $.post('eventos', $('form').serialize(), function () {
            carregar();
            $('form').each(function () {
                this.reset();
            });
        });
    });
    
    $('#btnEnviarRec').click(function () {
        var idEvento = $("#eventosForm input[name=id]").val();    
        $("#EventosRecForm input[name=eventos_id]").val(idEvento);    
        $.post('eventos_recursos', $('form[id=EventosRecForm]').serialize(), function () {
            carregarEventosRecursos(idEvento)();
            $('form').each(function () {
                this.reset();
            });
        });
    });

    $('#btnBuscar').click(function () {
        carregar();
        carregarLocais();
        carregarEventosRecursos();
    });
});

function formatDate(value) {
    if (value)
        return new Date(value).toLocaleDateString('pt-BR');
    return "";
}

function carregar() {
    $.getJSON('eventos', $('form[role=search]').serialize()).success(function (registros) {
        window.templateTr = window.templateTr || $('#divTable table tbody').html();
        var trHtml = window.templateTr;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{descricao\}\}/g, item.descricao)
                    .replace(/\{\{data\}\}/g, formatDate(item.data))
                    .replace(/\{\{hora\}\}/g, item.hora)
                    .replace(/\{\{qtd_pessoas\}\}/g, item.qtd_pessoas)
                    .replace(/\{\{locais_id\}\}/g, item.locais_id.descricao);
        });
        $('#divTable table tbody').html(respHtml);
    });
}

function carregarLocais(executa) {
    $.getJSON('locais').success(function(locais) {
        var options = "";
        locais.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.descricao + '</option>';
        });
        $('#eventosForm select[name=locais_id]').html(options);
        if (executa)
            executa();
    });
}

function editar(id) {
    $.getJSON("eventos?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("input[name=descricao]").val(data.descricao);
        $("input[name=data]").val(data.data);
        $("input[name=hora]").val(data.hora);
        $("input[name=qtd_pessoas]").val(data.qtd_pessoas);
        $("select[name=locais_id]").val(data.locais_id.id);     
        carregarEventosRecursos(name=id);
    });    
}

function excluir(id) {
    $.ajax("eventos?id=" + id, {
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
    $("input[name=data]").val("");
    $("input[name=hora]").val("");
    $("input[name=qtd_pessoas]").val("");
    $("select[name=locais_id]").val("");
    
//    Ver como resetar a tab dos recursos da maneira correta
//    carregarEventosRecursos(0);    
    carregarLocais();
}

function limparFormEventosRec() {
    $("input[name=id]").val("");
    $("input[name=eventos_id]").val("");
    $("input[name=recursos_id]").val("");
    $("input[name=qtd]").val("");
    $("input[name=valor]").val("");
    
}



function carregarEventosRecursos(eventos_id) {
    $.getJSON('eventos_recursos', 'eventos_id=' + eventos_id).success(function (registros) { 
        window.templateRec = window.templateRec || $('#divTableRec table tbody').html();
        var trHtml = window.templateRec;
        var respHtml = "";
        registros.forEach(function (item) {
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{eventos_id\}\}/g, item.eventos_id)
                    .replace(/\{\{recursos_id\}\}/g, item.recursos_id.descricao)
                    .replace(/\{\{qtd\}\}/g, item.qtd)
                    .replace(/\{\{valor\}\}/g, item.valor);
        });
        $('#divTableRec table tbody').html(respHtml);        
        carregarRecursos();
    });
}

function carregarRecursos(executa) {
    $.getJSON('recursos').success(function(recursos) {
        var options = "";
        recursos.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.descricao + '</option>';
        });
        $('#EventosRecForm select[name=recursos_id]').html(options);
        if (executa)
            executa();
    });
}

function editarRec(id) {
    $.getJSON("eventos_recursos?id=" + id).success(function (data) {
        $("input[name=id]").val(data.id);
        $("select[name=recursos_id]").val(data.recursos_id.id);
        $("input[name=qtd]").val(data.qtd);
        $("input[name=valor]").val(data.valor);    
    });    
}

function excluirRec(id) {
    $.ajax("eventos_recursos?id=" + id, {
        type: "DELETE"
    }).success(function () {
        var idEvento = $("#eventosForm input[name=id]").val();       
        carregarEventosRecursos(idEvento);
    }).error(function () {
        alert("Não é possível excluir esse registro pois ele possui dependências.");
    });
}
