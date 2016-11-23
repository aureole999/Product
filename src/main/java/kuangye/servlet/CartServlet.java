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
import java.util.Optional;
import java.util.stream.Collectors;

import static kuangye.constant.Constant.REQUEST_KEY_PRODUCT_ID;
import static kuangye.constant.Constant.SESSION_KEY_CART;

/**
 * Created by YE on 2016/11/15.
 * Add a product to Cart
 */
@WebServlet(urlPatterns = "/cart")
public class CartServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(CartServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setJsonHeader(resp);

        // get products in cart
        List<Product> cart = (List<Product>) req.getSession().getAttribute(SESSION_KEY_CART);
        if (cart == null) {
            cart = new ArrayList<>();
        } else {
            // clone cart to make sure thread safe
            cart = new ArrayList<>(cart);
        }

        // get products in stock
        List<Product> products = XmlDao.getInstance().getAll();

        Response response = new Response(products, cart);

        String productId = req.getParameter(REQUEST_KEY_PRODUCT_ID);
        if (productId == null) {
            resp.sendError(500);
            return;
        }

        // if product not exist in cart, add product to cart
        Optional<Product> newProduct = cart.stream().filter(p -> p.getId().equals(productId)).findFirst();
        if (!newProduct.isPresent()) {
            Product p = new Product(productId);
            p.setQuantity(1);
            cart.add(p);
        } else {
            newProduct.get().addQuantity(1);
        }

        // check cart product quantity
        cart = cart.stream().peek(c -> {
            // if not in stock
            int indexInStock = products.indexOf(c);
            if (indexInStock < 0) {
                c.setQuantity(0);
                response.addMessage("Can't find product: " + c.getId());
                return;
            }
            // if more than stock
            Product productInStock = products.get(indexInStock);
            c.setName(productInStock.getName());
            c.setPrice(productInStock.getPrice());
            long maxStock = productInStock.getStock();
            if (c.getQuantity() > maxStock) {
                c.setQuantity(maxStock);
                response.addMessage("Can't purchase any more " + productInStock.getName());
                return;
            }
        }).filter(c -> c.getQuantity() > 0).collect(Collectors.toList());

        response.setCart(cart);
        req.getSession().setAttribute(SESSION_KEY_CART, cart);
        sendJson(resp, response);
    }

}
