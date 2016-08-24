package br.com.eventos.dao;

import br.com.eventos.model.Locais;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocaisDao {

    public Locais inserir(Locais local) throws Exception {
        try {
            local.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO LOCAIS (ID, DESCRICAO) VALUES(?, ?)");
            stm.setLong(1, local.getId());
            stm.setString(2, local.getDescricao());
            stm.execute();

            return buscar(local.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Locais atualizar(Locais local) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE LOCAIS SET DESCRICAO = ? WHERE ID = ?");
            stm.setString(1, local.getDescricao());
            stm.setLong(2, local.getId());
            stm.execute();

            return buscar(local.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Locais local) throws Exception {
        excluir(local.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM LOCAIS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Locais> listarTodos(String nome) throws Exception {
        try {
            List<Locais> locals = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM LOCAIS WHERE UPPER(DESCRICAO) LIKE ? ORDER BY DESCRICAO");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM LOCAIS ORDER BY DESCRICAO");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                locals.add(lerRegistro(rs));
            }
            return locals;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public Locais buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM LOCAIS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('SEQ_LOCAIS')");
        rs.next();
        return rs.getLong(1);
    }

    private Locais lerRegistro(ResultSet rs) throws SQLException {
        Locais p = new Locais();
        p.setId(rs.getLong("id"));
        p.setDescricao(rs.getString("descricao"));
        return p;
    }

}
