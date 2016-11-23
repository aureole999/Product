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
 * Clear cart
 */
@WebServlet(urlPatterns = "/clear")
public class ClearCartServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(ClearCartServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setJsonHeader(resp);

        // remove cart
        req.getSession().removeAttribute(SESSION_KEY_CART);

        // get products in stock
        List<Product> products = XmlDao.getInstance().getAll();

        Response response = new Response(products, new ArrayList<>());

        sendJson(resp, response);
    }

}
