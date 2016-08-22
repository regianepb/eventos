package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class Produto implements Parseable {

    private Long id;
    private String codigo;
    private String nome;
    private BigDecimal preco;
    private BigDecimal estoque;

    public Produto() {
    }

    public Produto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.codigo);
        hash = 67 * hash + Objects.hashCode(this.nome);
        hash = 67 * hash + Objects.hashCode(this.preco);
        hash = 67 * hash + Objects.hashCode(this.estoque);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.preco, other.preco)) {
            return false;
        }
        return Objects.equals(this.estoque, other.estoque);
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"codigo\":\"%s\", \"nome\":\"%s\", \"preco\":\"%s\", \"estoque\":\"%s\"}", 
                id, codigo, nome, Utils.nullString(preco), Utils.nullString(estoque));
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        codigo = values.get("codigo");
        nome = values.get("nome");
        preco = Utils.parseDecimal(values.get("preco"));
        estoque = Utils.parseDecimal(values.get("estoque"));
    }
}
