package br.com.eventos.dao;

import br.com.eventos.model.Eventos;
import br.com.eventos.util.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventosDao {

    public Eventos inserir(Eventos evento) throws Exception {
        try {
            evento.setId(buscarProximoId());

            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO EVENTOS (ID, DESCRICAO, DATA_HORA, QTD_PESSOAS, LOCAIS_ID) VALUES(?, ?, ?, ?, ?)");
            stm.setLong(1, evento.getId());
            stm.setString(2, evento.getDescricao());
            stm.setTimestamp(3, Timestamp.valueOf(evento.getData_hora() == null ? LocalDateTime.now() : evento.getData_hora()));
            stm.setLong(4, evento.getQtd_pessoas());
            if (Utils.isNotNull(evento.getLocais_id(), evento.getLocais_id().getId())) {
                stm.setLong(5, evento.getLocais_id().getId());
            } else {
                stm.setNull(5, Types.INTEGER);
            }
            stm.execute();

            return buscar(evento.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao inserir o registro", ex);
        }
    }

    public Eventos atualizar(Eventos evento) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("UPDATE EVENTOS SET DESCRICAO = ?, DATA_HORA = ?, QTD_PESSOAS = ?, LOCAIS_ID = ? WHERE ID = ?");
            stm.setString(1, evento.getDescricao());
            if (Utils.isNotNull(evento.getData_hora())) {
                stm.setTimestamp(2, Timestamp.valueOf(evento.getData_hora()));
            } else {
                stm.setNull(2, Types.TIMESTAMP);
            }            
            stm.setLong(3, evento.getQtd_pessoas());
            if (evento.getLocais_id()!= null && evento.getLocais_id().getId() != null) {
                stm.setLong(4, evento.getLocais_id().getId());
            } else {
                stm.setNull(4, Types.INTEGER);
            }
            stm.setLong(5, evento.getId());
            stm.execute();

            return buscar(evento.getId());
        } catch (SQLException ex) {
            throw new Exception("Erro ao alterar o registro", ex);
        }
    }

    public void excluir(Eventos evento) throws Exception {
        excluir(evento.getId());
    }

    public void excluir(Long id) throws Exception {
        try {
            PreparedStatement stm = Connection.get().getParamStm("DELETE FROM EVENTOS WHERE ID = ?");
            stm.setLong(1, id);
            stm.execute();
        } catch (SQLException ex) {
            throw new Exception("Erro ao excluir o registro", ex);
        }
    }

    public List<Eventos> listarTodos(String nome) throws Exception {
        try {
            List<Eventos> eventos = new ArrayList<>();

            PreparedStatement stm;
            if (Utils.isNotEmpty(nome)) {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS WHERE UPPER(DESCRICAO) LIKE ? ORDER BY DESCRICAO");
                stm.setString(1, '%' + nome.toUpperCase().replaceAll(" ", "%") + '%');
            } else {
                stm = Connection.get().getParamStm("SELECT * FROM EVENTOS ORDER BY DESCRICAO");
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                eventos.add(lerRegistro(rs));
            }
            return eventos;
        } catch (SQLException ex) {
            throw new Exception("Erro ao listar os registro", ex);
        }
    }

    public Eventos buscar(Long id) throws Exception {
        if (id == null) {
            return null;
        } else {
            try {
                PreparedStatement stm = Connection.get().getParamStm("SELECT * FROM EVENTOS WHERE id = ?");
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('seq_evento')");
        rs.next();
        return rs.getLong(1);
    }

    private Eventos lerRegistro(ResultSet rs) throws SQLException {
        try {
            LocaisDao localDao = new LocaisDao();
            Eventos e = new Eventos();

            e.setId(rs.getLong("id"));
            e.setDescricao(rs.getString("descricao"));
            e.setData_hora(rs.getTimestamp("data_hora") == null ? null : rs.getTimestamp("data_hora").toLocalDateTime());
            e.setId(rs.getLong("qtd_pessoas"));
            e.setLocais_id(localDao.buscar(rs.getLong("locais_id")));

            return e;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

}