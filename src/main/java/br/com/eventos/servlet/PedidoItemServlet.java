package br.com.eventos.servlet;

import br.com.eventos.dao.PedidoItemDao;
import br.com.eventos.model.PedidoItem;
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

@WebServlet("pedidos/itens")
public class PedidoItemServlet extends HttpServlet {

    private PedidoItemDao dao = new PedidoItemDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        if (Utils.isNotEmpty(req.getParameter("idPedido")) || Utils.isNotEmpty(req.getParameter("id"))) {
            try (PrintWriter writer = resp.getWriter()) {
                if (Utils.isNotEmpty(req.getParameter("id"))) {
                    // ler apenas um registro
                    PedidoItem item = dao.buscar(Utils.parseLong(req.getParameter("id")));
                    writer.append(item.toString());
                } else {
                    // ler todos
                    List<PedidoItem> itens = dao.listarTodos(Utils.parseLong(req.getParameter("idPedido")));
                    writer.append(Utils.convertListToString(itens));
                }
            } catch (Exception ex) {
                Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        } else {
            resp.sendError(400, "Código do pedido é obrigatório!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        if (Utils.isNotEmpty(req.getParameter("idPedido"))) {
            try (PrintWriter writer = resp.getWriter()) {
                PedidoItem item = new PedidoItem();
                item.parse(Utils.getParameterMap(req));
                if (Utils.isNotEmpty(req.getParameter("id"))) {
                    item = dao.atualizar(item);
                } else {
                    item = dao.inserir(Utils.parseLong(req.getParameter("idPedido")), item);
                }
                writer.append(item.toString());
            } catch (Exception ex) {
                Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        } else {
            resp.sendError(400, "Código do pedido é obrigatório!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.excluir(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
                resp.sendError(500, ex.getMessage());
            }
        }
    }
}
