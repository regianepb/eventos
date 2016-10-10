EventosDaoTest.java

import br.com.eventos.dao.EventosDao;
import br.com.eventos.model.Eventos;
import static br.com.eventos.util.Utils.parseDate;
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
    private final Long qtd_pessoas = Long.parseLong("350");
    
    @Test
    public void testInsertEvento() throws Exception {
        Eventos evento = new Eventos();
        
        evento.setDescricao(descricao);
        evento.setData(data);        
        evento.setHora(hora);        
        evento.setQtd_pessoas(qtd_pessoas);        
        
        evento = eventoDao.inserir(evento);
        
        assertNotNull(evento);
        assertEquals(evento.getDescricao(), descricao);
        assertEquals(evento.getData(), data);
        assertEquals(evento.getHora(), hora);
        assertEquals(evento.getQtd_pessoas(), qtd_pessoas);
    }    
    
}


-------------------------------------------------
trigger e procedure

DROP TRIGGER IF EXISTS tg_checagem_eventos ON eventos;

CREATE TRIGGER tg_checagem_eventos before INSERT, UPDATE ON eventos
FOR EACH ROW EXECUTE PROCEDURE f_checagem_eventos();
	  
CREATE OR REPLACE FUNCTION f_checagem_eventos() RETURNS trigger AS $$
    DECLARE w_existe_evento integer;
    BEGIN
        
	SELECT count(*) 
	  INTO w_existe_evento
	  FROM eventos 
	 WHERE DESCRICAO = NEW.DESCRICAO and
		   data = new.data and
		   hora = new.hora;
			   
	if w_existe_evento > 0 then
		RAISE EXCEPTION 'Já existe um evento cadastrado com essa mesma descrição, data e horário. (%)', NEW.descricao||' - '||NEW.data||' '||NEW.hora;
    end if;	
        
		
END;
$$ LANGUAGE plpgsql;	  

