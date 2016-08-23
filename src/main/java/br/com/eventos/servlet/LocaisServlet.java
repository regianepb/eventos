package br.com.eventos.servlet;

import br.com.eventos.dao.LocaisDao;
import br.com.eventos.model.Locais;
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

@WebServlet("locais")
public class LocaisServlet extends HttpServlet {

    private LocaisDao dao = new LocaisDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                Locais l = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(l.toString());
            } else {
                // ler todos
                List<Locais> locais = dao.listarTodos(req.getParameter("filter"));
                writer.append(Utils.convertListToString(locais));
            }
        } catch (Exception ex) {
            Logger.getLogger(LocaisServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Locais l = new Locais();
            l.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                l = dao.atualizar(l);
            } else {
                l = dao.inserir(l);
            }
            writer.append(l.toString());
        } catch (Exception ex) {
            Logger.getLogger(LocaisServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LocaisServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
