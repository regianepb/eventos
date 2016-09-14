package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.math.BigDecimal;
import java.util.Map;

public class EventosRecursos implements Parseable {
    private Long id;
    private Eventos eventos_id;
    private Recursos recursos_id;
    private BigDecimal qtd;
    private BigDecimal valor;

    public EventosRecursos() {
    }

    public EventosRecursos(Long id) {
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

    public Recursos getRecursos_id() {
        return recursos_id;
    }

    public void setRecursos_id(Recursos recursos_id) {
        this.recursos_id = recursos_id;
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
        return String.format("{\"id\":\"%s\", \"eventos_id\":%s, \"recursos_id\":%s, \"qtd\":\"%s\", \"valor\":\"%s\"}", 
                id, eventos_id, recursos_id, qtd, valor);
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        eventos_id = Utils.isEmpty(values.get("eventos_id")) ? null : new Eventos(Utils.parseLong(values.get("eventos_id")));                       
        recursos_id = Utils.isEmpty(values.get("recursos_id")) ? null : new Recursos(Utils.parseLong(values.get("recursos_id")));
        qtd = Utils.parseDecimal(values.get("qtd"));
        valor = Utils.parseDecimal(values.get("valor"));
    }
    
    
}

