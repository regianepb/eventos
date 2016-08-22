package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.util.Map;

public class ClassifDespesas implements Parseable {
    private Long id;
    private String descricao;

    public ClassifDespesas() {
    }

    public ClassifDespesas(Long id) {
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

            
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"descricao\":\"%s\"}", 
                id, descricao);
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        descricao = values.get("descricao");
    }
    
    
    
}

