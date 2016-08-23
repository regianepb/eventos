package br.com.eventos.dao;

import br.com.eventos.model.Despesas;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DespesasDao {

    public Despesas inserir(Despesas despesas) throws Exception {
        try {
            despesas.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO DESPESAS (ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(?, ?, ?)");
            stm.setLong(1, despesas.getId());
            stm.setString(2, despesas.getDescricao());
            if (Utils.isNotNull(despesas.getClassif_despesas_id(), despesas.getClassif_despesas_id().getId())) {
                stm.setLong(3, despesas.getClassif_despesas_id().getId());
            } else {
                stm.setNull(3, Types.INTEGER);
            }
            stm.execute();

            return buscar(despesas.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Despesas atualizar(Despesas despesas) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE DESPESAS SET DESCRICAO = ?, CLASSIF_DESPESAS_ID = ? WHERE ID = ?");
            stm.setString(1, despesas.getDescricao());
            if (despesas.getClassif_despesas_id()!= null && despesas.getClassif_despesas_id().getId() != null) {
                stm.setLong(2, despesas.getClassif_despesas_id().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }            
            stm.setLong(3, despesas.getId());
            stm.execute();

            return buscar(despesas.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Despesas despesas) throws Exception {
        excluir(despesas.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM DESPESAS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Despesas> listarTodos(String nome) throws Exception {
        try {
            List<Despesas> despesas = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM DESPESAS WHERE UPPER(DESCRICAO) LIKE ? ORDER BY DESCRICAO");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM DESPESAS ORDER BY DESCRICAO");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                despesas.add(lerRegistro(rs));
            }
            return despesas;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public Despesas buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM DESPESAS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_despesas')");
        rs.next();
        return rs.getLong(1);
    }

    private Despesas lerRegistro(ResultSet rs) throws SQLException {
        try {
            Despesas p = new Despesas();
            ClassifDespesasDao classifDao = new ClassifDespesasDao();

            p.setId(rs.getLong("id"));
            p.setDescricao(rs.getString("descricao"));
            p.setClassif_despesas_id(classifDao.buscar(rs.getLong("classif_despesas_id")));
            return p;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

}
