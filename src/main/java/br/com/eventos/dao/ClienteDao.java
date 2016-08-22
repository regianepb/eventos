package br.com.eventos.dao;

import br.com.eventos.model.Cliente;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClienteDao {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    
    public Cliente inserir(Cliente cliente) throws Exception {
        try {
            cliente.setId(buscarProximoId());
            
            if (Utils.isEmpty(cliente.getCodigo())) {
                cliente.setCodigo(sdf.format(new Date()) + String.format("-%06d", cliente.getId()));
            }

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO clientes (id, codigo, nome, documento, telefone, email) VALUES(?, ?, ?, ?, ?, ?)");
            stm.setLong(1, cliente.getId());
            stm.setString(2, cliente.getCodigo());
            stm.setString(3, cliente.getNome());
            stm.setString(4, cliente.getDocumento());
            stm.setString(5, cliente.getTelefone());
            stm.setString(6, cliente.getEmail());
            stm.execute();

            return buscar(cliente.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Cliente atualizar(Cliente cliente) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE clientes SET codigo = ?, nome = ?, documento = ?, telefone = ?, email = ? WHERE id = ?");
            stm.setString(1, cliente.getCodigo());
            stm.setString(2, cliente.getNome());
            stm.setString(3, cliente.getDocumento());
            stm.setString(4, cliente.getTelefone());
            stm.setString(5, cliente.getEmail());
            stm.setLong(6, cliente.getId());
            stm.execute();

            return buscar(cliente.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Cliente cliente) throws Exception {
        excluir(cliente.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM clientes WHERE id = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Cliente> listarTodos(String nome) throws Exception {
        try {
            List<Cliente> clientes = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM clientes WHERE UPPER(nome) like ? ORDER BY nome");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM clientes ORDER BY nome");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                clientes.add(lerRegistro(rs));
            }
            return clientes;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registros", ex);
        }
    }

    public Cliente buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM clientes WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_clientes')");
        rs.next();
        return rs.getLong(1);
    }

    private Cliente lerRegistro(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setCodigo(rs.getString("codigo"));
        c.setNome(rs.getString("nome"));
        c.setDocumento(rs.getString("documento"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        return c;
    }

}
