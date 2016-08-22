package br.com.eventos.dao;

import br.com.eventos.model.Pedido;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {

    public Pedido inserir(Pedido pedido) throws Exception {
        try {
            pedido.setId(buscarProximoId());
            pedido.setNumero(String.format("%06d", pedido.getId()));
            
            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO pedidos (id, numero, emissao, id_cliente, forma_pagto, condicao_pagto, desconto) VALUES(?, ?, ?, ?, ?, ?, ?)");
            stm.setLong(1, pedido.getId());
            stm.setString(2, pedido.getNumero());
            stm.setTimestamp(3, Timestamp.valueOf(pedido.getEmissao() == null ? LocalDateTime.now() : pedido.getEmissao()));
            if (Utils.isNotNull(pedido.getCliente(), pedido.getCliente().getId())) {
                stm.setLong(4, pedido.getCliente().getId());
            } else {
                stm.setNull(4, Types.INTEGER);
            }
            stm.setString(5, pedido.getFormaPagto());
            stm.setString(6, pedido.getCondicaoPagto());
            if (Utils.isNotNull(pedido.getDesconto())) {
                stm.setBigDecimal(7, pedido.getDesconto());
            } else {
                stm.setNull(7, Types.NUMERIC);
            }
            stm.execute();

            return buscar(pedido.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Pedido atualizar(Pedido pedido) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE pedidos SET aprovacao = ?, id_cliente = ?, forma_pagto = ?, condicao_pagto = ?, desconto = ? WHERE id = ?");
            if (Utils.isNotNull(pedido.getAprovacao())) {
                stm.setTimestamp(1, Timestamp.valueOf(pedido.getAprovacao()));
            } else {
                stm.setNull(1, Types.TIMESTAMP);
            }
            if (pedido.getCliente() != null && pedido.getCliente().getId() != null) {
                stm.setLong(2, pedido.getCliente().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }
            stm.setString(3, pedido.getFormaPagto());
            stm.setString(4, pedido.getCondicaoPagto());
            if (Utils.isNotNull(pedido.getDesconto())) {
                stm.setBigDecimal(5, pedido.getDesconto());
            } else {
                stm.setNull(5, Types.NUMERIC);
            }
            stm.setLong(6, pedido.getId());
            stm.execute();

            return buscar(pedido.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Pedido pedido) throws Exception {
        excluir(pedido.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM pedidos WHERE id = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Pedido> listarTodos() throws Exception {
        try {
            List<Pedido> pedidos = new ArrayList<>();

            Statement stm = Connection.get().getStm();
            ResultSet rs = stm.executeQuery("SELECT * FROM pedidos ORDER BY numero");

            while (rs.next()) {
                pedidos.add(lerRegistro(rs));
            }
            return pedidos;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registros", ex);
        }
    }

    public Pedido buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM pedidos WHERE id = ?");
                stm.setLong(1, id);
                ResultSet rs = stm.executeQuery();
                rs.next();
                return lerRegistro(rs);
            } catch (SQLException ex) {
                throw new Exception("Erro ao buscar o registro", ex);
            }
        }
    }

    private Long buscarProximoId() throws SQLException {
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_pedidos')");
        rs.next();
        return rs.getLong(1);
    }

    private Pedido lerRegistro(ResultSet rs) throws SQLException {
        try {
            ClienteDao cliDao = new ClienteDao();
            PedidoItemDao itemDao = new PedidoItemDao();

            Pedido p = new Pedido();
            p.setId(rs.getLong("id"));
            p.setNumero(rs.getString("numero"));
            p.setCliente(cliDao.buscar(rs.getLong("id_cliente")));
            p.setEmissao(rs.getTimestamp("emissao") == null ? null : rs.getTimestamp("emissao").toLocalDateTime());
            p.setAprovacao(rs.getTimestamp("aprovacao") == null ? null : rs.getTimestamp("aprovacao").toLocalDateTime());
            p.setCondicaoPagto(rs.getString("condicao_pagto"));
            p.setFormaPagto(rs.getString("forma_pagto"));
            p.setValorTotal(rs.getBigDecimal("valor_total"));
            p.setDesconto(rs.getBigDecimal("desconto"));
            p.setItens(itemDao.listarTodos(rs.getLong("id")));
            return p;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}
