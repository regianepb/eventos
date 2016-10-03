$(function () {
    carregar();
    carregarLocais();
    carregarDespesas();

    $('#btnEnviar').click(function () {
        $.post('eventos', $('form[id=eventosForm]').serialize(), function () {
            limparForm();
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
            limparFormEventosRec();
            carregarEventosRecursos(idEvento)();
            $('eventosRecForm').each(function () {
                this.reset();
            });
        });
    });

    $('#btnEnviarDesp').click(function () {
        var idEvento = $("#eventosForm input[name=id]").val();
        $("#EventosDespForm input[name=eventos_id]").val(idEvento);
        $.post('eventos_despesas', $('form[id=EventosDespForm]').serialize(), function () {
            limparFormEventosDesp();
            carregarEventosDespesas(idEvento)();
            $('eventosDespForm').each(function () {
                this.reset();
            });
        });
    });

    $('#btnBuscar').click(function () {
        carregar();
        carregarLocais();
        carregarEventosRecursos();
        carregarEventosDespesas();
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
    $.getJSON('locais').success(function (locais) {
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
        $("form[id=eventosForm] input[name=id]").val(data.id);
        $("form[id=eventosForm] input[name=descricao]").val(data.descricao);
        $("form[id=eventosForm] input[name=data]").val(data.data);
        $("form[id=eventosForm] input[name=hora]").val(data.hora);
        $("form[id=eventosForm] input[name=qtd_pessoas]").val(data.qtd_pessoas);
        $("form[id=eventosForm] select[name=locais_id]").val(data.locais_id.id);
        carregarEventosRecursos(name = id);
        carregarEventosDespesas(name = id);
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
    $("form[id=eventosForm] input[name=id]").val("");
    $("form[id=eventosForm] input[name=descricao]").val("");
    $("form[id=eventosForm] input[name=data]").val("");
    $("form[id=eventosForm] input[name=hora]").val("");
    $("form[id=eventosForm] input[name=qtd_pessoas]").val("");
    $("form[id=eventosForm] select[name=locais_id]").val("");

//    Ver como resetar a tab dos recursos da maneira correta
//    carregarEventosRecursos(0);    
    carregarLocais();
}

function limparFormEventosRec() {
    $("form[id=EventosRecForm] input[name=id]").val("");
    $("form[id=EventosRecForm] input[name=eventos_id]").val("");
    $("form[id=EventosRecForm] select[name=recursos_id]").val("");
    $("form[id=EventosRecForm] input[name=qtd]").val("");
    $("form[id=EventosRecForm] input[name=valor]").val("");
    $("form[id=EventosRecForm] input[name=total]").val("");
}

function carregarEventosRecursos(eventos_id) {
    $.getJSON('eventos_recursos', 'eventos_id=' + eventos_id).success(function (registros) {
        window.templateRec = window.templateRec || $('#divTableRec table tbody').html();
        var trHtml = window.templateRec;
        var respHtml = "";
        var totalDosRec = 0;
        registros.forEach(function (item) {
            var total = item.qtd * item.valor;
            totalDosRec += total;
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{eventos_id\}\}/g, item.eventos_id)
                    .replace(/\{\{recursos_id\}\}/g, item.recursos_id.descricao)
                    .replace(/\{\{qtd\}\}/g, item.qtd)
                    .replace(/\{\{valor\}\}/g, item.valor)
                    .replace(/\{\{total\}\}/g, total.toFixed(2));
        });
        $('#divTableRec table tbody').html(respHtml);

//        window.templateRecFoot = $('#divTabletTotRec table tfoot').html();
//        var trHtmlFoot = $('#divTabletTotRec table tfoot').html();
//        var respHtmlFoot = "";
//        respHtmlFoot = trHtmlFoot.replace(/\{\{vlrTotalRec\}\}/g, totalDosRec.toFixed(2));            
//        
//        $('#divTableTotRec table tfoot').html(respHtmlFoot);        
        carregarRecursos();
    });

}

function carregarRecursos(executa) {
    $.getJSON('recursos').success(function (recursos) {
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
        $("form[id=EventosRecForm] input[name=id]").val(data.id);
        $("form[id=EventosRecForm] input[name=eventos_id]").val(data.eventos_id.id);
        $("form[id=EventosRecForm] select[name=recursos_id]").val(data.recursos_id.id);
        $("form[id=EventosRecForm] input[name=qtd]").val(data.qtd);
        $("form[id=EventosRecForm] input[name=valor]").val(data.valor);
        calculaTotalRecurso();
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

function calculaTotalRecurso() {
    var qtd = $("#EventosRecForm input[name=qtd]").val();
    var valor = $("#EventosRecForm input[name=valor]").val();
    var total = qtd * valor;
    if (total > 0) {
        $("#EventosRecForm input[name=total]").val(total);
    } else {
        $("#EventosRecForm input[name=total]").val("");
    };
}


//Despesas
function limparFormEventosDesp() {
    $("form[id=EventosDespForm] input[name=id]").val("");
    $("form[id=EventosDespForm] input[name=eventos_id]").val("");
    $("form[id=EventosDespForm] select[name=despesas_id]").val("");
    $("form[id=EventosDespForm] input[name=qtd]").val("");
    $("form[id=EventosDespForm] input[name=valor]").val("");
    $("form[id=EventosDespForm] input[name=total]").val("");
}

function carregarEventosDespesas(eventos_id) {
    $.getJSON('eventos_despesas', 'eventos_id=' + eventos_id).success(function (registros) {
        window.templateDesp = window.templateDesp || $('#divTableDesp table tbody').html();
        var trHtml = window.templateDesp;
        var respHtml = "";
        var totalDesp = 0;
        registros.forEach(function (item) {
            var total = item.qtd * item.valor;
            totalDesp += total;
            respHtml += trHtml
                    .replace(/\{\{id\}\}/g, item.id)
                    .replace(/\{\{eventos_id\}\}/g, item.eventos_id)
                    .replace(/\{\{despesas_id\}\}/g, item.despesas_id.descricao)
                    .replace(/\{\{qtd\}\}/g, item.qtd)
                    .replace(/\{\{valor\}\}/g, item.valor)
                    .replace(/\{\{total\}\}/g, total.toFixed(2));
        });
        $('#divTableDesp table tbody').html(respHtml);

        carregarDespesas();
    });

}

function carregarDespesas(executa) {
    $.getJSON('despesas').success(function (despesas) {
        var options = "";
        despesas.forEach(function (item) {
            options += '<option value="' + item.id + '">' + item.descricao + '</option>';
        });
        $('#EventosDespForm select[name=despesas_id]').html(options);
        if (executa)
            executa();
    });
}

function editarDesp(id) {
    $.getJSON("eventos_despesas?id=" + id).success(function (data) {
        $("form[id=EventosDespForm] input[name=id]").val(data.id);
        $("form[id=EventosDespForm] input[name=eventos_id]").val(data.eventos_id.id);
        $("form[id=EventosDespForm] select[name=despesas_id]").val(data.despesas_id.id);
        $("form[id=EventosDespForm] input[name=qtd]").val(data.qtd);
        $("form[id=EventosDespForm] input[name=valor]").val(data.valor);
        calculaTotalDespesa();
    });
}

function excluirDesp(id) {
    $.ajax("eventos_despesas?id=" + id, {
        type: "DELETE"
    }).success(function () {
        var idEvento = $("#eventosForm input[name=id]").val();
        carregarEventosDespesas(idEvento);
    }).error(function () {
        alert("Não é possível excluir esse registro pois ele possui dependências.");
    });
}

function calculaTotalDespesa() {
    var qtd = $("#EventosDespForm input[name=qtd]").val();
    var valor = $("#EventosDespForm input[name=valor]").val();
    var total = qtd * valor;
    if (total > 0) {
        $("#EventosDespForm input[name=total]").val(total);
    } else {
        $("#EventosDespForm input[name=total]").val("");
    };
}


function verificarTab() {
    var idEvento = $("#eventosForm input[name=id]").val();
    if (idEvento === "") {
        $('.nav-tabs a[href="#eventos"]').tab('show');
        alert("O evento ainda não foi gravado. Para inserir os recursos e despesas é necessário gravar o Evento.");
    } else {
//        alert("gravado");
    };
}