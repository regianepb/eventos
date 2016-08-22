package br.com.eventos.servlet;

import br.com.eventos.dao.PedidoDao;
import br.com.eventos.model.Pedido;
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

@WebServlet("pedidos")
public class PedidoServlet extends HttpServlet {

    private PedidoDao dao = new PedidoDao();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                // ler apenas um registro
                Pedido ped = dao.buscar(Utils.parseLong(req.getParameter("id")));
                writer.append(ped.toString());
            } else {
                // ler todos
                List<Pedido> pedidos = dao.listarTodos();
                writer.append(Utils.convertListToString(pedidos));
            }
        } catch (Exception ex) {
            Logger.getLogger(PedidoServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        try (PrintWriter writer = resp.getWriter()) {
            Pedido ped = new Pedido();
            ped.parse(Utils.getParameterMap(req));
            if (Utils.isNotEmpty(req.getParameter("id"))) {
                ped = dao.atualizar(ped);
            } else {
                ped = dao.inserir(ped);
            }
            writer.append(ped.toString());
        } catch (Exception ex) {
            Logger.getLogger(PedidoServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.excluir(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                Logger.getLogger(PedidoServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }
}
