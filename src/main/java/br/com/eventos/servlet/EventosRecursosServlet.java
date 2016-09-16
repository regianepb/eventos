package br.com.eventos.servlet;

import br.com.eventos.dao.EventosRecursosDao;
import br.com.eventos.model.EventosRecursos;
import br.com.eventos.util.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("eventos_recursos")
public class EventosRecursosServlet extends HttpServlet {

    private EventosRecursosDao dao = new EventosRecursosDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        if (Utils.isNotEmpty(req.getParameter("eventos_id")) || Utils.isNotEmpty(req.getParameter("id"))) {
            try (PrintWriter writer = resp.getWriter()) {
                if (Utils.isNotEmpty(req.getParameter("id"))) {
                    // ler apenas um registro
                    EventosRecursos eventoRec = dao.buscar(Utils.parseLong(req.getParameter("id")));
                    writer.append(eventoRec.toString());
                } else {
                    // ler todos
                    List<EventosRecursos> eventosRec = dao.listarTodos(Utils.parseLong(req.getParameter("eventos_id")));
                    writer.append(Utils.convertListToString(eventosRec));
                }
            } catch (Exception ex) {
                Logger.getLogger(EventosRecursosServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        } else {
            resp.sendError(400, "Código do evento é obrigatório!");
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        
        if (Utils.isNotEmpty(req.getParameter("eventos_id"))) {
            try (PrintWriter writer = resp.getWriter()) {
                EventosRecursos eventoRec = new EventosRecursos();
                eventoRec.parse(Utils.getParameterMap(req));
                if (Utils.isNotEmpty(req.getParameter("id"))) {
                    System.out.println("Atualiza eventos_recursos");
                    eventoRec = dao.atualizar(eventoRec);
                } else {
                    System.out.println("Insere eventos_recursos");
                    eventoRec = dao.inserir(Utils.parseLong(req.getParameter("eventos_id")), eventoRec);
                }
                writer.append(eventoRec.toString());
            } catch (Exception ex) {
                Logger.getLogger(EventosRecursosServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        } else {
            resp.sendError(400, "Código do evento é obrigatório!");
        }
    }
    
    
    

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.excluir(Utils.parseLong(req.getParameter("id")));
                resp.getWriter().append("#" + req.getParameter("id") + " exclu&iacute;do com sucesso!!!");
            } catch (Exception ex) {
                Logger.getLogger(EventosRecursosServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
