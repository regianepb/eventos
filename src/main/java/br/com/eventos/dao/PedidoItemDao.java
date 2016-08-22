package br.com.eventos.dao;

import br.com.eventos.model.PedidoItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoItemDao {

    public PedidoItem inserir(Long idPedido, PedidoItem item) throws Exception {
        try {
            item.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO pedido_itens (id, id_pedido, id_produto, valor_unitario, quantidade) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, item.getId());
            stm.setLong(2, idPedido);
            stm.setLong(3, item.getProduto().getId());
            stm.setBigDecimal(4, item.getValorUnitario());
            stm.setBigDecimal(5, item.getQuantidade());
            stm.execute();

            return buscar(item.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public PedidoItem atualizar(PedidoItem item) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE pedido_itens SET id_produto = ?, valor_unitario = ?, quantidade = ? WHERE id = ?");
            stm.setLong(1, item.getProduto().getId());
            stm.setBigDecimal(2, item.getValorUnitario());
            stm.setBigDecimal(3, item.getQuantidade());
            stm.setLong(4, item.getId());
            stm.execute();

            return buscar(item.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(PedidoItem item) throws Exception {
        excluir(item.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM pedido_itens WHERE id = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<PedidoItem> listarTodos(Long idPedido) throws Exception {
        try {
            List<PedidoItem> itens = new ArrayList<>();

            PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM pedido_itens WHERE id_pedido = ? ORDER BY id");
            stm.setLong(1, idPedido);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                itens.add(lerRegistro(rs));
            }
            return itens;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registros", ex);
        }
    }

    public PedidoItem buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM pedido_itens WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_pedido_itens')");
        rs.next();
        return rs.getLong(1);
    }

    private PedidoItem lerRegistro(ResultSet rs) throws SQLException {
        try {
            ProdutoDao prodDao = new ProdutoDao();
            
            PedidoItem p = new PedidoItem();
            p.setId(rs.getLong("id"));
            p.setProduto(prodDao.buscar(rs.getLong("id_produto")));
            p.setValorUnitario(rs.getBigDecimal("valor_unitario"));
            p.setQuantidade(rs.getBigDecimal("quantidade"));
            p.setValorTotal(rs.getBigDecimal("valor_total"));
            return p;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

}
