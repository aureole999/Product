package kuangye.servlet;

import kuangye.entity.Product;
import kuangye.xml.XmlDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kuangye.constant.Constant.SESSION_KEY_CART;

/**
 * Created by YE on 2016/11/15.
 * Get all products in stock and cart
 */
@WebServlet(urlPatterns = "/list")
public class ListServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(ListServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setJsonHeader(resp);
        List<Product> stocks = XmlDao.getInstance().getAll();
        List<Product> cart = (List<Product>) req.getSession().getAttribute(SESSION_KEY_CART);
        if (cart == null) {
            cart = new ArrayList<>();
        }
        Response result = new Response(stocks, cart);
        log.info("list " + stocks.size() + " products");
        sendJson(resp, result);
    }

}


