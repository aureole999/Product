package kuangye.xml;

import kuangye.entity.Product;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import javax.servlet.ServletException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by YE on 2016/11/17.
 */
public class XmlDaoTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void getAll() throws Exception {
        List<Product> all = XmlDao.getInstance().getAll();
        Assert.assertEquals(6, all.size());
    }

    @Test
    public void update() throws Exception {
        List<Product> before = XmlDao.getInstance().getAll();
        long stockBefore = before.get(0).getStock();
        long quantity = 1;

        List<Product> update = new ArrayList<>();
        update.add(before.get(0));
        update.get(0).setQuantity(quantity);

        XmlDao.getInstance().update(update);

        List<Product> after = XmlDao.getInstance().getAll();
        Assert.assertEquals(stockBefore - quantity, after.get(0).getStock());
    }

    @Test
    public void update_multi() throws Exception {
        List<Product> before = XmlDao.getInstance().getAll();
        long stockBefore = before.get(0).getStock();
        long quantity = 1;

        List<Product> update = new ArrayList<>();
        update.add(before.get(0));
        update.get(0).setQuantity(quantity);

        int count = 100;

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            es.execute(() -> {
                try {
                    XmlDao.getInstance().update(update);
                } catch (ServletException e) {
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);
        List<Product> after = XmlDao.getInstance().getAll();
        Assert.assertEquals(Math.max(stockBefore - quantity * count, 0), after.get(0).getStock());
    }

    @Before
    public void setUp() throws Exception {
        FileUtils.copyFileToDirectory(
                new File(getClass().getClassLoader().getResource("products.xml").getFile()),
                folder.getRoot()
        );
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.copyFile(
                new File(folder.getRoot().getAbsoluteFile() + File.separator + "products.xml"),
                new File(getClass().getClassLoader().getResource("products.xml").getFile())
        );
    }
}