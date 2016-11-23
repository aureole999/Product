package kuangye.servlet;

import kuangye.constant.Constant;
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

/**
 * Created by YE on 2016/11/15.
 * Checkout products in cart
 */
@WebServlet(urlPatterns = "/checkout")
public class CheckoutServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(CheckoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setJsonHeader(resp);

        Response response = new Response();
        List<Product> cart = (List<Product>) req.getSession().getAttribute(Constant.SESSION_KEY_CART);
        if (cart == null) {
            response.setStocks(XmlDao.getInstance().getAll());
            response.setCart(new ArrayList<>());
            response.addMessage("No product to checkout.");
            log.info("No product to checkout.");
            sendJson(resp, response);
            return;
        }

        // checkout products in cart
        boolean updateSuccess = XmlDao.getInstance().update(cart);

        List<Product> products = XmlDao.getInstance().getAll();
        response.setStocks(products);
        if (updateSuccess) {
            // clear cart
            response.setCart(new ArrayList<>());
            req.getSession().removeAttribute(Constant.SESSION_KEY_CART);
        } else {
            response.setCart(cart);
            response.addMessage("No enough stock");
        }

        sendJson(resp, response);
        return;
    }
}


