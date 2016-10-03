package br.com.eventos.dao;

import br.com.eventos.model.EventosDespesas;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EventosDespesasDao {

    public EventosDespesas inserir(Long idEvento, EventosDespesas everec) throws Exception {
        try {
            everec.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO EVENTOS_DESPESAS (ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, everec.getId());
            stm.setLong(2, idEvento);
            if (Utils.isNotNull(everec.getDespesas_id(), everec.getDespesas_id().getId())) {
                stm.setLong(3, everec.getDespesas_id().getId());
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
    

    public EventosDespesas atualizar(EventosDespesas evedesp) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE EVENTOS_DESPESAS SET EVENTOS_ID = ?, DESPESAS_ID = ?, QTD = ?, VALOR = ? WHERE ID = ?");
            if (evedesp.getEventos_id()!= null && evedesp.getEventos_id().getId() != null) {
                stm.setLong(1, evedesp.getEventos_id().getId());
            } else {
                stm.setNull(1, Types.INTEGER);
            }
            if (evedesp.getDespesas_id()!= null && evedesp.getDespesas_id().getId() != null) {
                stm.setLong(2, evedesp.getDespesas_id().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }
            stm.setBigDecimal(3, evedesp.getQtd());
            stm.setBigDecimal(4, evedesp.getValor());
            stm.setLong(5, evedesp.getId());
            stm.execute();

            return buscar(evedesp.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(EventosDespesas evedesp) throws Exception {
        excluir(evedesp.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM EVENTOS_DESPESAS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<EventosDespesas> listarTodos(Long eventos_id) throws Exception {
        try {
            List<EventosDespesas> itens = new ArrayList<>();

            PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_DESPESAS WHERE EVENTOS_ID = ? ORDER BY ID");
            stm.setLong(1, eventos_id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                itens.add(lerRegistro(rs));
            }
            return itens;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registros", ex);
        }
    }
    

    public EventosDespesas buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_DESPESAS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('SEQ_EVENTOS_DESPESAS')");
        rs.next();
        return rs.getLong(1);
    }

    private EventosDespesas lerRegistro(ResultSet rs) throws SQLException {
        try {
            EventosDao eventoDao = new EventosDao();
            DespesasDao despesaDao = new DespesasDao();
            EventosDespesas e = new EventosDespesas();

            e.setId(rs.getLong("id"));
            e.setEventos_id(eventoDao.buscar(rs.getLong("eventos_id")));
            e.setDespesas_id(despesaDao.buscar(rs.getLong("despesas_id")));
            e.setQtd(rs.getBigDecimal("qtd"));
            e.setValor(rs.getBigDecimal("valor"));

            return e;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

}
