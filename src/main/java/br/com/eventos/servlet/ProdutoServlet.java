package br.com.eventos.servlet;

import br.com.eventos.dao.ProdutoDao;
import br.com.eventos.model.Produto;
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

@WebServlet("produtos")
public class ProdutoServlet extends HttpServlet {

    private ProdutoDao dao = new ProdutoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                Produto pro = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(pro.toString());
            } else {
                // ler todos
                List<Produto> produtos = dao.listarTodos(req.getParameter("filter"));
                writer.append(Utils.convertListToString(produtos));
            }
        } catch (Exception ex) {
            Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.resetBuffer();
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Produto pro = new Produto();
            pro.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                pro = dao.atualizar(pro);
            } else {
                pro = dao.inserir(pro);
            }
            writer.append(pro.toString());
        } catch (Exception ex) {
            Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.reset();
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.excluir(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.reset();
                resp.sendError(500, ex.getMessage());
            }
        }
    }
}
