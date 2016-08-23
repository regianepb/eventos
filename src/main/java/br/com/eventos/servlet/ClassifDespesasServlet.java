package br.com.eventos.servlet;

import br.com.eventos.dao.ClassifDespesasDao;
import br.com.eventos.model.ClassifDespesas;
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

@WebServlet("classifdespesas")
public class ClassifDespesasServlet extends HttpServlet {

    private ClassifDespesasDao dao = new ClassifDespesasDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                ClassifDespesas c = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(c.toString());
            } else {
                // ler todos
                List<ClassifDespesas> classifdespesas = dao.listarTodos(req.getParameter("filter"));
                writer.append(Utils.convertListToString(classifdespesas));
            }
        } catch (Exception ex) {
            Logger.getLogger(ClassifDespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            ClassifDespesas c = new ClassifDespesas();
            c.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                c = dao.atualizar(c);
            } else {
                c = dao.inserir(c);
            }
            writer.append(c.toString());
        } catch (Exception ex) {
            Logger.getLogger(ClassifDespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClassifDespesasServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
