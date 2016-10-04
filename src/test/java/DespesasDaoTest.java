
import br.com.eventos.dao.DespesasDao;
import br.com.eventos.model.Despesas;
import org.junit.Assert;
import org.junit.Test;



public class DespesasDaoTest {

    private final DespesasDao dao = new DespesasDao();
    
    @Test
    public void testInsertSemClassif() throws Exception {
        Despesas desp = new Despesas();
        desp.setDescricao("Material de limpeza");
        
        desp = dao.inserir(desp);
        
//        Assert.assertNotNull(desp.getId());
//        Assert.assertNull(desp.getClassif_despesas_id());
    }
    
    
    
}

