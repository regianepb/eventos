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

    public EventosDespesas inserir(EventosDespesas evedesp) throws Exception {
        try {
            evedesp.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO EVENTOS_DESPESAS (ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, evedesp.getId());
            if (Utils.isNotNull(evedesp.getEventos_id(), evedesp.getEventos_id().getId())) {
                stm.setLong(2, evedesp.getEventos_id().getId());
            } else {
                stm.setNull(2, Types.INTEGER);
            }
            if (Utils.isNotNull(evedesp.getDespesas_id(), evedesp.getDespesas_id().getId())) {
                stm.setLong(3, evedesp.getDespesas_id().getId());
            } else {
                stm.setNull(3, Types.INTEGER);
            }
            stm.setBigDecimal(4, evedesp.getQtd());
            stm.setBigDecimal(5, evedesp.getValor());
            stm.execute();

            return buscar(evedesp.getId());
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

    public List<EventosDespesas> listarTodos(String nome) throws Exception {
        try {
            List<EventosDespesas> evedesps = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_DESPESAS WHERE EVENTOS_ID = ? ORDER BY EVENTOS_ID");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS_DESPESAS ORDER BY EVENTOS_ID");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                evedesps.add(lerRegistro(rs));
            }
            return evedesps;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_evedesp')");
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
