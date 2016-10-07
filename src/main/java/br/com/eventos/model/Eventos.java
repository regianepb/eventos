package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class Eventos implements Parseable {
    private Long id;
    private String descricao;
    LocalDate data;
    LocalTime hora;
    private Long qtd_pessoas;
    private Locais locais_id;

    public Eventos() {
    }

    public Eventos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }


    public Long getQtd_pessoas() {
        return qtd_pessoas;
    }

    public void setQtd_pessoas(Long qtd_pessoas) {
        this.qtd_pessoas = qtd_pessoas;
    }

    public Locais getLocais_id() {
        return locais_id;
    }

    public void setLocais_id(Locais locais_id) {
        this.locais_id = locais_id;
    }

              
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"descricao\":\"%s\", \"data\":\"%s\", \"hora\":\"%s\", \"qtd_pessoas\":\"%s\", \"locais_id\":%s}", 
                id, descricao, data, hora, qtd_pessoas, locais_id);
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        descricao = values.get("descricao");
        data = Utils.parseDate(values.get("data"), "dd/MM/yyyy");
        hora = Utils.parseTime(values.get("hora"), "hh:mm");
        qtd_pessoas = Utils.parseLong(values.get("qtd_pessoas"));
        locais_id = Utils.isEmpty(values.get("locais_id")) ? null : new Locais(Utils.parseLong(values.get("locais_id")));                       
    }
}
