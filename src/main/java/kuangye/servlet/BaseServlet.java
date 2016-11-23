package kuangye.servlet;

import com.google.gson.Gson;
import kuangye.entity.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YE on 2016/11/16.
 */
public class BaseServlet extends HttpServlet {

    protected void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
    }

    /**
     * Write JSON to response
     *
     * @param resp response
     * @param obj JSON obj
     * @throws IOException
     */
    protected void sendJson(HttpServletResponse resp, Object obj) throws IOException {
        Gson g = new Gson();
        PrintWriter out = resp.getWriter();
        out.print(g.toJson(obj));
        out.flush();
    }

    /**
     * Created by YE on 2016/11/16.
     */
    public static class Response {
        private List<Product> stocks;
        private List<Product> cart;
        private List<String> messages;

        public Response(List<Product> stocks, List<Product> cart) {
            this.stocks = stocks;
            this.cart = cart;
        }

        public Response() {
        }

        public List<Product> getStocks() {
            return stocks;
        }

        public void setStocks(List<Product> stocks) {
            this.stocks = stocks;
        }

        public List<Product> getCart() {
            return cart;
        }

        public void setCart(List<Product> cart) {
            this.cart = cart;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }

        public void addMessage(String message) {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }
            this.messages.add(message);
        }
    }
}
