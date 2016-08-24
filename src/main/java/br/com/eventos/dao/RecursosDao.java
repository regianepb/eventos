package br.com.eventos.dao;

import br.com.eventos.model.Recursos;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecursosDao {

    public Recursos inserir(Recursos recurso) throws Exception {
        try {
            recurso.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO RECURSOS (ID, DESCRICAO) VALUES(?, ?)");
            stm.setLong(1, recurso.getId());
            stm.setString(2, recurso.getDescricao());
            stm.execute();

            return buscar(recurso.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Recursos atualizar(Recursos recurso) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE RECURSOS SET DESCRICAO = ? WHERE ID = ?");
            stm.setString(1, recurso.getDescricao());
            stm.setLong(2, recurso.getId());
            stm.execute();

            return buscar(recurso.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Recursos recurso) throws Exception {
        excluir(recurso.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM RECURSOS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Recursos> listarTodos(String nome) throws Exception {
        try {
            List<Recursos> recursos = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM RECURSOS WHERE UPPER(DESCRICAO) LIKE ? ORDER BY DESCRICAO");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM RECURSOS ORDER BY DESCRICAO");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                recursos.add(lerRegistro(rs));
            }
            return recursos;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public Recursos buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM RECURSOS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('SEQ_RECURSOS')");
        rs.next();
        return rs.getLong(1);
    }

    private Recursos lerRegistro(ResultSet rs) throws SQLException {
        Recursos p = new Recursos();
        p.setId(rs.getLong("id"));
        p.setDescricao(rs.getString("descricao"));
        return p;
    }

}
