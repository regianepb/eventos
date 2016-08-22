package br.com.eventos.dao;

import br.com.eventos.model.Produto;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    public Produto inserir(Produto produto) throws Exception {
        try {
            produto.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO produtos (id, codigo, nome, estoque, preco) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, produto.getId());
            stm.setString(2, produto.getCodigo());
            stm.setString(3, produto.getNome());
            stm.setBigDecimal(4, produto.getEstoque());
            stm.setBigDecimal(5, produto.getPreco());
            stm.execute();

            return buscar(produto.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Produto atualizar(Produto produto) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE produtos SET codigo = ?, nome = ?, estoque = ?, preco = ? WHERE id = ?");
            stm.setString(1, produto.getCodigo());
            stm.setString(2, produto.getNome());
            stm.setBigDecimal(3, produto.getEstoque());
            stm.setBigDecimal(4, produto.getPreco());
            stm.setLong(5, produto.getId());
            stm.execute();

            return buscar(produto.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Produto produto) throws Exception {
        excluir(produto.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM produtos WHERE id = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Produto> listarTodos(String nome) throws Exception {
        try {
            List<Produto> produtos = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM produtos WHERE UPPER(nome) like ? ORDER BY nome");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM produtos ORDER BY nome");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                produtos.add(lerRegistro(rs));
            }
            return produtos;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public Produto buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM produtos WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_produtos')");
        rs.next();
        return rs.getLong(1);
    }

    private Produto lerRegistro(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getLong("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNome(rs.getString("nome"));
        p.setPreco(rs.getBigDecimal("preco"));
        p.setEstoque(rs.getBigDecimal("estoque"));
        return p;
    }

}
