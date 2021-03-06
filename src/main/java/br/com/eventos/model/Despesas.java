package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.util.Map;

public class Despesas implements Parseable {
    private Long id;
    private String descricao;
    private ClassifDespesas classif_despesas_id;
    
    public Despesas() {
    }

    public Despesas(Long id) {
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

    public ClassifDespesas getClassif_despesas_id() {
        return classif_despesas_id;
    }

    public void setClassif_despesas_id(ClassifDespesas classif_despesas_id) {
        this.classif_despesas_id = classif_despesas_id;
    }

        
    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"descricao\":\"%s\", \"classif_despesas_id\":%s}", 
                                id, descricao, classif_despesas_id);
    }
    

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        descricao = values.get("descricao");
        classif_despesas_id = Utils.isEmpty(values.get("classif_despesas_id")) ? null : new ClassifDespesas(Utils.parseLong(values.get("classif_despesas_id")));                       
    }
    
}