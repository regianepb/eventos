package br.com.eventos.dao;

import br.com.eventos.model.Eventos;
import br.com.eventos.util.Utils;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class EventosDao {

    public Eventos inserir(Eventos evento) throws Exception {
        
        try {
            evento.setId(buscarProximoId());
                
            PreparedStatement stm = Connection.get().getParamStm("INSERT INTO EVENTOS (ID, DESCRICAO, DATA, HORA, QTD_PESSOAS, LOCAIS_ID) VALUES(?, ?, ?, '20:00', ?, ?)");
            stm.setLong(1, evento.getId());
            stm.setString(2, evento.getDescricao());      
            stm.setDate(3, Date.valueOf(evento.getData()));
//            stm.setTime(4, Time.valueOf(evento.getHora()));   
            /*stm.setDate(3, Date.valueOf(evento.getData() == null ? LocalDate.now() : evento.getData()));
            stm.setTime(4, Time.valueOf(evento.getHora() == null ? LocalTime.now() : evento.getHora()));*/
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
            PreparedStatement stm = Connection.get().getParamStm("UPDATE EVENTOS SET DESCRICAO = ?, DATA = ?, HORA = ?, QTD_PESSOAS = ?, LOCAIS_ID = ? WHERE ID = ?");
            stm.setString(1, evento.getDescricao());
            stm.setString(2, evento.getData());
            stm.setString(3, evento.getHora());
            /*
            if (Utils.isNotNull(evento.getData())) {
                stm.setDate(2, Date.valueOf(evento.getData()));
            } else {
                stm.setNull(2, Types.DATE);
            } 
            if (Utils.isNotNull(evento.getHora())) {
                stm.setTime(3, Time.valueOf(evento.getHora()));
            } else {
                stm.setNull(3, Types.TIME);
            } */
            stm.setLong(4, evento.getQtd_pessoas());
            if (evento.getLocais_id()!= null && evento.getLocais_id().getId() != null) {
                stm.setLong(5, evento.getLocais_id().getId());
            } else {
                stm.setNull(5, Types.INTEGER);
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
        ResultSet rs = Connection.get().getStm().executeQuery("SELECT NEXTVAL('SEQ_EVENTOS')");
        rs.next();
        return rs.getLong(1);
    }

    private Eventos lerRegistro(ResultSet rs) throws SQLException {
        try {
            LocaisDao localDao = new LocaisDao();
            Eventos e = new Eventos();

            e.setId(rs.getLong("id"));
            e.setDescricao(rs.getString("descricao"));
            e.setData(rs.getString("data"));
            e.setHora(rs.getString("hora"));
            /*e.setData(rs.getDate("data") == null ? null : rs.getDate("data").toLocalDate());
            e.setHora(rs.getTime("hora") == null ? null : rs.getTime("hora").toLocalTime());*/
            e.setQtd_pessoas(rs.getLong("qtd_pessoas"));
            e.setLocais_id(localDao.buscar(rs.getLong("locais_id")));

            return e;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    private Date Date(String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Time Time(String hora) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Date LocalDate(String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Time LocalTime(String hora) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
