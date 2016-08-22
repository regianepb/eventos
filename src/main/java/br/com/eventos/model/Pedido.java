package br.com.eventos.model;

import br.com.eventos.util.Utils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pedido implements Parseable {

    private Long id;
    private String numero;
    private LocalDateTime emissao;
    private LocalDateTime aprovacao;
    private Cliente cliente;
    private String formaPagto;
    private String condicaoPagto;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private List<PedidoItem> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDateTime emissao) {
        this.emissao = emissao;
    }

    public LocalDateTime getAprovacao() {
        return aprovacao;
    }

    public void setAprovacao(LocalDateTime aprovacao) {
        this.aprovacao = aprovacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }

    public String getCondicaoPagto() {
        return condicaoPagto;
    }

    public void setCondicaoPagto(String condicaoPagto) {
        this.condicaoPagto = condicaoPagto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public List<PedidoItem> getItens() {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.numero);
        hash = 67 * hash + Objects.hashCode(this.emissao);
        hash = 67 * hash + Objects.hashCode(this.aprovacao);
        hash = 67 * hash + Objects.hashCode(this.cliente);
        hash = 67 * hash + Objects.hashCode(this.formaPagto);
        hash = 67 * hash + Objects.hashCode(this.condicaoPagto);
        hash = 67 * hash + Objects.hashCode(this.valorTotal);
        hash = 67 * hash + Objects.hashCode(this.desconto);
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
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.formaPagto, other.formaPagto)) {
            return false;
        }
        if (!Objects.equals(this.condicaoPagto, other.condicaoPagto)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.emissao, other.emissao)) {
            return false;
        }
        if (!Objects.equals(this.aprovacao, other.aprovacao)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.valorTotal, other.valorTotal)) {
            return false;
        }
        return Objects.equals(this.desconto, other.desconto);
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"numero\":\"%s\", \"emissao\":\"%s\", \"aprovacao\":\"%s\", \"cliente\":%s, \"formaPagto\":\"%s\", \"condicaoPagto\":\"%s\", \"valorTotal\":\"%s\", \"desconto\":\"%s\", \"itens\":%s}",
                id, numero, emissao, Utils.nullString(aprovacao), cliente, formaPagto, condicaoPagto, Utils.nullString(valorTotal), Utils.nullString(desconto), itens);
    }

    @Override
    public void parse(Map<String, String> values) {
        id = Utils.parseLong(values.get("id"));
        numero = values.get("numero");
        emissao = Utils.parseDate(values.get("emissao"), "dd/MM/yyyy HH:mm:ss");
        aprovacao = Utils.parseDate(values.get("aprovacao"), "dd/MM/yyyy HH:mm:ss");
        cliente = Utils.isEmpty(values.get("cliente")) ? null : new Cliente(Utils.parseLong(values.get("cliente")));
        formaPagto = values.get("formaPagto");
        condicaoPagto = values.get("condicaoPagto");
        valorTotal = Utils.parseDecimal(values.get("valorTotal"));
        desconto = Utils.parseDecimal(values.get("desconto"));
    }
}
