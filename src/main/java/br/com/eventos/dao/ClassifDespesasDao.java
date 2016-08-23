package br.com.eventos.dao;

import br.com.eventos.model.ClassifDespesas;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class ClassifDespesasDao {

    public ClassifDespesas inserir(ClassifDespesas classifDesp) throws Exception {
        try {
            classifDesp.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO CLASSIF_DESPESAS (ID, DESCRICAO) VALUES(?, ?)");
            stm.setLong(1, classifDesp.getId());
            stm.setString(2, classifDesp.getDescricao());
            stm.execute();

            return buscar(classifDesp.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public ClassifDespesas atualizar(ClassifDespesas classifDesp) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE CLASSIF_DESPESAS SET DESCRICAO = ? WHERE ID = ?");
            stm.setString(1, classifDesp.getDescricao());
            stm.setLong(2, classifDesp.getId());
            stm.execute();

            return buscar(classifDesp.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(ClassifDespesas classifDesp) throws Exception {
        excluir(classifDesp.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM CLASSIF_DESPESAS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<ClassifDespesas> listarTodos(String nome) throws Exception {
        try {
            List<ClassifDespesas> classifDesps = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM CLASSIF_DESPESAS WHERE UPPER(DESCRICAO) LIKE ? ORDER BY DESCRICAO");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM CLASSIF_DESPESAS ORDER BY DESCRICAO");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                classifDesps.add(lerRegistro(rs));
            }
            return classifDesps;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public ClassifDespesas buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM CLASSIF_DESPESAS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_classifDesps')");
        rs.next();
        return rs.getLong(1);
    }

    private ClassifDespesas lerRegistro(ResultSet rs) throws SQLException {
        ClassifDespesas p = new ClassifDespesas();
        p.setId(rs.getLong("id"));
        p.setDescricao(rs.getString("descricao"));
        return p;
    }

}
