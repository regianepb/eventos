package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.math.BigDecimal;
import java.util.Map;

public class EventosDespesas implements Parseable {
    private Long id;
    private Eventos eventos_id;
    private Despesas despesas_id;
    private BigDecimal qtd;
    private BigDecimal valor;

    public EventosDespesas() {
    }

    public EventosDespesas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Eventos getEventos_id() {
        return eventos_id;
    }

    public void setEventos_id(Eventos eventos_id) {
        this.eventos_id = eventos_id;
    }

    public Despesas getDespesas_id() {
        return despesas_id;
    }

    public void setDespesas_id(Despesas despesas_id) {
        this.despesas_id = despesas_id;
    }

    public BigDecimal getQtd() {
        return qtd;
    }

    public void setQtd(BigDecimal qtd) {
        this.qtd = qtd;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
       
              
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"eventos_id\":\"%s\", \"despesas_id\":\"%s\", \"qtd\":\"%s\", \"valor\":\"%s\"}", 
                id, eventos_id, despesas_id, qtd, valor);
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        eventos_id = Utils.isEmpty(values.get("eventos_id")) ? null : new Eventos(Utils.parseLong(values.get("eventos_id")));                       
        despesas_id = Utils.isEmpty(values.get("despesas_id")) ? null : new Despesas(Utils.parseLong(values.get("despesas_id")));
        qtd = Utils.parseDecimal(values.get("qtd"));
        valor = Utils.parseDecimal(values.get("valor"));
    }
}

