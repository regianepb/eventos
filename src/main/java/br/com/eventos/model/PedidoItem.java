package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class PedidoItem implements Parseable {

    private Long id;
    private Produto produto;
    private BigDecimal valorUnitario;
    private BigDecimal quantidade;
    private BigDecimal valorTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.produto);
        hash = 67 * hash + Objects.hashCode(this.valorUnitario);
        hash = 67 * hash + Objects.hashCode(this.quantidade);
        hash = 67 * hash + Objects.hashCode(this.valorTotal);
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
        final PedidoItem other = (PedidoItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.produto, other.produto)) {
            return false;
        }
        if (!Objects.equals(this.valorUnitario, other.valorUnitario)) {
            return false;
        }
        if (!Objects.equals(this.quantidade, other.quantidade)) {
            return false;
        }
        return Objects.equals(this.valorTotal, other.valorTotal);
    }

    @Override
    public String toString() {
        return String.format("{\"id\": \"%s\", \"produto\": %s, \"valorUnitario\": \"%s\", \"quantidade\": \"%s\", \"valorTotal\": \"%s\"}",
                id, produto, Utils.nullString(valorUnitario), Utils.nullString(quantidade), Utils.nullString(valorTotal));
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        produto = Utils.isEmpty(values.get("produto")) ? null : new Produto(Utils.parseLong(values.get("produto")));
        valorUnitario = Utils.parseDecimal(values.get("valorUnitario"));
        quantidade = Utils.parseDecimal(values.get("quantidade"));
        valorTotal = Utils.parseDecimal(values.get("valorTotal"));
    }
}
