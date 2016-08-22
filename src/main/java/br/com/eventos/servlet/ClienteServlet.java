package br.com.eventos.servlet;

import br.com.eventos.dao.ClienteDao;
import br.com.eventos.model.Cliente;
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

@WebServlet("clientes")
public class ClienteServlet extends HttpServlet {

    private ClienteDao dao = new ClienteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                Cliente cli = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(cli.toString());
            } else {
                // ler todos
                List<Cliente> clientes = dao.listarTodos(req.getParameter("filter"));
                writer.append(Utils.convertListToString(clientes));
            }
        } catch (Exception ex) {
            Logger.getLogger(ClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Cliente cli = new Cliente();
            cli.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                cli = dao.atualizar(cli);
            } else {
                cli = dao.inserir(cli);
            }
            writer.append(cli.toString());
        } catch (Exception ex) {
            Logger.getLogger(ClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }

}
