$(function () {
    carregar();
    carregarLocais();
    carregarDespesas();
    
    $('#btnNvoEvento').click(function () {
        $('a[data-toggle="tab"]').hide();        
    });
        
    $('#btnEnviar').click(function () {
        $.post('eventos', $('form[id=eventosForm]').serialize(), function () {
            $('a[data-toggle="tab"]').show();        
            limparTodasForm();
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
            limparFormEventosRecModal();
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
            limparFormEventosDespModal();
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

    $('#btnFechar').click(function () {
        limparTodasForm();
    });


});

function formatDate(value) {
    var data = value.split('-');
    return data[2] + "/"+ data[1] + "/" + data[0];
}

function formatNumber(value) {
    if (value)
        return new Number(value).toLocaleString('pt-BR', {minimumFractionDigits: 2});
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
    $('#eventos').focus();
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
    $("#eventosForm input[name=id]").val("");
    $("#eventosForm input[name=descricao]").val("");
    $("#eventosForm input[name=data]").val("");
    $("#eventosForm input[name=hora]").val("");
    $("#eventosForm input[name=qtd_pessoas]").val("");
    $("#eventosForm select[name=locais_id]").val("");
}

function limparFormEventosRec() {
    window.templateRec = window.templateRec || $('#divTableRec table tbody').html();
    var respHtml = window.templateRec;
    $('#divTableRec table tbody').html(respHtml);

    window.templateRecTot = window.templateRecTot || $('#divTableRecTot table tbody').html();
    var trHtmlTot = window.templateRecTot;
    var respHtmlTot = trHtmlTot.replace(/\{\{vlrTotalRec\}\}/g, 0);
    $('#divTableRecTot table tbody').html(respHtmlTot);
}

function limparFormEventosRecModal() {
    $("#EventosRecForm input[name=id]").val("");
    $("#EventosRecForm input[name=eventos_id]").val("");
    $("#EventosRecForm select[name=recursos_id]").val("");
    $("#EventosRecForm input[name=qtd]").val("");
    $("#EventosRecForm input[name=valor]").val("");
    $("#EventosRecForm input[name=total]").val("");
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
                    .replace(/\{\{qtd\}\}/g, formatNumber(item.qtd))
                    .replace(/\{\{valor\}\}/g, formatNumber(item.valor))
                    .replace(/\{\{total\}\}/g, formatNumber(total.toFixed(2)));
        });
        $('#divTableRec table tbody').html(respHtml);

        window.templateRecTot = window.templateRecTot || $('#divTableRecTot table tbody').html();
        var trHtmlTot = window.templateRecTot;
        var respHtmlTot = trHtmlTot.replace(/\{\{vlrTotalRec\}\}/g, formatNumber(totalDosRec.toFixed(2)));

        $('#divTableRecTot table tbody').html(respHtmlTot);
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
        $("#EventosRecForm input[name=total]").val(formatNumber(total));
    } else {
        $("#EventosRecForm input[name=total]").val(formatNumber(0));
    }
    ;
}


//Despesas
function limparFormEventosDesp() {
    window.templateDesp = window.templateDesp || $('#divTableDesp table tbody').html();
    var respHtml = window.templateDesp;
    $('#divTableDesp table tbody').html(respHtml);

    window.templateDespTot = window.templateDespTot || $('#divTableDespTot table tbody').html();
    var trHtmlTot = window.templateDespTot;
    var respHtmlTot = trHtmlTot.replace(/\{\{vlrTotalDesp\}\}/g, 0);
    $('#divTableDespTot table tbody').html(respHtmlTot);
}

function limparFormEventosDespModal() {
    $("#EventosDespForm input[name=id]").val("");
    $("#EventosDespForm input[name=eventos_id]").val("");
    $("#EventosDespForm select[name=despesas_id]").val("");
    $("#EventosDespForm input[name=qtd]").val("");
    $("#EventosDespForm input[name=valor]").val("");
    $("#EventosDespForm input[name=total]").val("");
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
                    .replace(/\{\{qtd\}\}/g, formatNumber(item.qtd))
                    .replace(/\{\{valor\}\}/g, formatNumber(item.valor))
                    .replace(/\{\{total\}\}/g, formatNumber(total.toFixed(2)));
        });
        $('#divTableDesp table tbody').html(respHtml);

        window.templateDespTot = window.templateDespTot || $('#divTableDespTot table tbody').html();
        var trHtmlTot = window.templateDespTot;
        var respHtmlTot = trHtmlTot.replace(/\{\{vlrTotalDesp\}\}/g, formatNumber(totalDesp.toFixed(2)));

        $('#divTableDespTot table tbody').html(respHtmlTot);
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
        $("#EventosDespForm input[name=total]").val(formatNumber(total));
    } else {
        $("#EventosDespForm input[name=total]").val(formatNumber(0));
    }
    ;
}


function verificarTab() {

//    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
//        var target = e.target.attributes.href.value;
//        alert(target);
//  $('#eventos').focus();
//    });
////    var idEvento = $("#eventosForm input[name=id]").val();
//    if (idEvento === "") {
//        $('.nav-tabs a[href="#eventos"]').tab('show');
//        alert("O evento ainda não foi gravado. Para inserir os recursos e despesas é necessário gravar o Evento.");
//    } else {
////        alert("gravado");
//    };
}


function calcularResumo() {
    var totalGeralRec = 0;
    var totalGeralDesp = 0;
    var total = 0;
    var saldo = 0;
    var idEvento = $("#eventosForm input[name=id]").val();

    $.getJSON('eventos_recursos', 'eventos_id=' + idEvento).success(function (registros) {
        registros.forEach(function (item) {
            total = item.qtd * item.valor;
            totalGeralRec += total;
        });
        saldo = totalGeralRec;
        $("#resumoForm input[name=total_rec_res]").val(formatNumber(totalGeralRec.toFixed(2)));
        $("#resumoForm input[name=saldo_res]").val(formatNumber(saldo.toFixed(2)));
    });

    $.getJSON('eventos_despesas', 'eventos_id=' + idEvento).success(function (registros) {
        registros.forEach(function (item) {
            total = item.qtd * item.valor;
            totalGeralDesp += total;
        });
        saldo -= totalGeralDesp;
        $("#resumoForm input[name=total_desp_res]").val(formatNumber(totalGeralDesp.toFixed(2)));
        $("#resumoForm input[name=saldo_res]").val(formatNumber(saldo.toFixed(2)));
    });
}

function limparFormResumo() {
    $("#resumoForm input[name=total_rec_res]").val("");
    $("#resumoForm input[name=total_desp_res]").val("");
    $("#resumoForm input[name=saldo_res]").val("");
}

function limparTodasForm() {
    limparForm();
    limparFormEventosRec();
    limparFormEventosDesp();
    limparFormResumo();
}

