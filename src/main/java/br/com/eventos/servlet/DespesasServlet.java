package br.com.eventos.servlet;

import br.com.eventos.dao.DespesasDao;
import br.com.eventos.model.Despesas;
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

@WebServlet("despesas")
public class DespesasServlet extends HttpServlet {

    private DespesasDao dao = new DespesasDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                Despesas d = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(d.toString());
            } else {
                // ler todos                
                List<Despesas> despesas = dao.listarTodos(req.getParameter("filter"));            
                writer.append(Utils.convertListToString(despesas));
            }
        } catch (Exception ex) {
            Logger.getLogger(DespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Despesas d = new Despesas();
            d.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                d = dao.atualizar(d);
            } else {
                d = dao.inserir(d);
            }
            writer.append(d.toString());
        } catch (Exception ex) {
            Logger.getLogger(DespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(DespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
