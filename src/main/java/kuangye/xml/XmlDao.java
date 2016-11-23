package kuangye.xml;

import kuangye.entity.Product;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Kuang on 2016/11/16.
 *
 * Read and write XML for product
 */
public class XmlDao {

    private static final Logger log = Logger.getLogger(XmlDao.class);
    private static final String fileName = "products.xml";
    private static final String rootElement = "products";
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static XmlDao ourInstance = new XmlDao();

    public static XmlDao getInstance() {
        return ourInstance;
    }

    private XmlDao() {
    }

    /**
     * Get all products
     *
     * @return products
     */
    public List<Product> getAll() throws ServletException {
        lock.readLock().lock();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        final List<Product> result = new ArrayList<>();

        try {
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                boolean isInRoot = false;

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    // convert XML element to Product obj
                    if (isInRoot) {

                        Map<String, String> properties = new HashMap<>();
                        for (int i = 0; i < attributes.getLength(); i++) {
                            properties.put(attributes.getQName(i), attributes.getValue(i));
                        }
                        properties.put("type", qName);

                        Product product = new Product(properties.remove("id"));
                        product.setName(properties.remove("name"));
                        product.setType(properties.remove("type"));

                        String stock = properties.remove("stock");
                        if (stock != null) {
                            product.setStock(Long.parseLong(stock));
                        }
                        String price = properties.remove("price");
                        if (price != null) {
                            product.setPrice(Long.parseLong(price));
                        }
                        product.setProperties(properties);

                        result.add(product);
                    }
                    if (rootElement.equalsIgnoreCase(qName)) {
                        isInRoot = true;
                    }
                    super.startElement(uri, localName, qName, attributes);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    //System.out.println("End Element :" + qName);
                    if (rootElement.equalsIgnoreCase(qName)) {
                        isInRoot = false;
                    }
                    super.endElement(uri, localName, qName);
                }

            };
            saxParser.parse(this.getClass().getClassLoader().getResource(this.fileName).getFile(), handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e);
            throw new ServletException(e);
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    /**
     * Checkout products in cart and reduce the stock
     *
     * @param cart products in cart
     * @return True if success, Otherwise False
     * @throws ServletException
     */
    public boolean update(List<Product> cart) throws ServletException {
        lock.writeLock().lock();

        List<Product> all = getAll();

        try {
            for (Product product : all) {
                int index = cart.indexOf(product);
                if (index >= 0) {
                    if (product.getStock() >= cart.get(index).getQuantity()) {
                        product.setStock(product.getStock() - cart.get(index).getQuantity());
                    } else {
                        return false;
                    }
                }
            }
            writeToXml(all);

        } catch (XMLStreamException | IOException e) {
            log.error(e);
            throw new ServletException(e);
        } finally {
            lock.writeLock().unlock();
        }

        return true;
    }

    /**
     * Write all product in list to XML
     *
     * @param products products to write
     * @throws XMLStreamException
     * @throws IOException
     */
    private void writeToXml(List<Product> products) throws XMLStreamException, IOException {

        try (FileWriter stringWriter = new FileWriter(this.getClass().getClassLoader().getResource(fileName).getFile())) {

            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.writeStartElement(rootElement);

            for (Product product : products) {
                xmlStreamWriter.writeStartElement(product.getType());
                xmlStreamWriter.writeAttribute("id", product.getId());
                xmlStreamWriter.writeAttribute("name", product.getName());
                xmlStreamWriter.writeAttribute("price", String.valueOf(product.getPrice()));
                xmlStreamWriter.writeAttribute("stock", String.valueOf(product.getStock()));
                for (Map.Entry<String, String> entry : product.getProperties().entrySet()) {
                    xmlStreamWriter.writeAttribute(entry.getKey(), entry.getValue());
                }
                xmlStreamWriter.writeEndElement();
            }

            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        }

    }
}