package br.com.eventos.dao;


import br.com.eventos.dao.EventosDao;
import br.com.eventos.model.Eventos;
import br.com.eventos.model.Locais;
import static br.com.eventos.util.Utils.parseDate;
import static br.com.eventos.util.Utils.parseLong;
import static br.com.eventos.util.Utils.parseTime;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class EventosDaoTest {
    
    private final EventosDao eventoDao = new EventosDao();
    private final String descricao = "Festa de Final de Ano da Empresa X";
    private final LocalDate data = parseDate("2016-11-25");
    private final LocalTime hora = parseTime("20:00");
    private final Long qtd_pessoas = parseLong("350");
    
    @Test
    public void testInsertEvento() throws Exception {
        Eventos evento = new Eventos();
        Locais local = new Locais();
        
        evento.setDescricao(descricao);
        evento.setData(data);
        evento.setHora(hora);
        evento.setQtd_pessoas(qtd_pessoas);
        evento.setLocais_id(local);
        
        evento = eventoDao.inserir(evento);
        evento = eventoDao.inserir(evento);
        
        assertNotNull(evento);
        assertEquals(evento.getDescricao(), descricao);
        assertEquals(evento.getData(), data);
        assertEquals(evento.getHora(), hora);
        assertEquals(evento.getQtd_pessoas(), qtd_pessoas);
        
        eventoDao.excluir(evento.getId());
        
    }
}
