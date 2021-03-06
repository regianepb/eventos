package br.com.eventos.servlet;

import br.com.eventos.dao.EventosDao;
import br.com.eventos.model.Eventos;
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

@WebServlet("eventos")
public class EventosServlet extends HttpServlet {

    private EventosDao dao = new EventosDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {            
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro                
                Eventos e = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(e.toString());
            } else {
                // ler todos                
                List<Eventos> eventos = dao.listarTodos(req.getParameter("filter"));
                writer.append(Utils.convertListToString(eventos));
            }
        } catch (Exception ex) {
            Logger.getLogger(EventosServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Eventos e = new Eventos();
            e.parse(Utils.getParameterMap(req));            
            if (Utils.isNotEmpty(req.getParameter("id"))) {                
                e = dao.atualizar(e);
            } else {                
                e = dao.inserir(e);
            }
            writer.append(e.toString());
        } catch (Exception ex) {
            Logger.getLogger(EventosServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.excluir(Utils.parseLong(req.getParameter("id")));
                resp.getWriter().append("#" + req.getParameter("id") + " exclu&iacute;do com sucesso!!!");
            } catch (Exception ex) {
                Logger.getLogger(EventosServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
