package br.com.eventos.dao;

import br.com.eventos.model.EventosRecursos;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EventosRecursosDao {

    public EventosRecursos inserir(EventosRecursos everec) throws Exception {
        try {
            everec.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO EVENTOS_RECURSOS (ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, everec.getId());
            if (Utils.isNotNull(everec.getEventos_id(), everec.getEventos_id().getId())) {
                stm.setLong(2, everec.getEventos_id().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }
            if (Utils.isNotNull(everec.getRecursos_id(), everec.getRecursos_id().getId())) {
                stm.setLong(3, everec.getRecursos_id().getId());
            } else {
                stm.setNull(3, Types.INTEGER);
            }
            stm.setBigDecimal(4, everec.getQtd());
            stm.setBigDecimal(5, everec.getValor());
            stm.execute();

            return buscar(everec.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public EventosRecursos atualizar(EventosRecursos everec) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE EVENTOS_RECURSOS SET EVENTOS_ID = ?, RECURSOS_ID = ?, QTD = ?, VALOR = ? WHERE ID = ?");
            if (everec.getEventos_id()!= null && everec.getEventos_id().getId() != null) {
                stm.setLong(1, everec.getEventos_id().getId());
            } else {
                stm.setNull(1, Types.INTEGER);
            }
            if (everec.getRecursos_id()!= null && everec.getRecursos_id().getId() != null) {
                stm.setLong(2, everec.getRecursos_id().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }
            stm.setBigDecimal(3, everec.getQtd());
            stm.setBigDecimal(4, everec.getValor());
            stm.setLong(5, everec.getId());
            stm.execute();

            return buscar(everec.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(EventosRecursos everec) throws Exception {
        excluir(everec.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM EVENTOS_RECURSOS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<EventosRecursos> listarTodos(String nome) throws Exception {
        try {
            List<EventosRecursos> everecs = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_RECURSOS WHERE EVENTOS_ID = ? ORDER BY EVENTOS_ID");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_RECURSOS ORDER BY EVENTOS_ID");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                everecs.add(lerRegistro(rs));
            }
            return everecs;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public EventosRecursos buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_RECURSOS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('SEQ_EVENTOS_RECURSOS')");
        rs.next();
        return rs.getLong(1);
    }

    private EventosRecursos lerRegistro(ResultSet rs) throws SQLException {
        try {
            EventosDao eventoDao = new EventosDao();
            RecursosDao recesaDao = new RecursosDao();
            EventosRecursos e = new EventosRecursos();

            e.setId(rs.getLong("id"));
            e.setEventos_id(eventoDao.buscar(rs.getLong("eventos_id")));
            e.setRecursos_id(recesaDao.buscar(rs.getLong("recursos_id")));
            e.setQtd(rs.getBigDecimal("qtd"));
            e.setValor(rs.getBigDecimal("valor"));

            return e;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

}
