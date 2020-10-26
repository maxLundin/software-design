package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

class GetProductsServletTest extends BaseTest {

    @Test
    public void testEmpty() throws IOException {
        GetProductsServlet servlet = new GetProductsServlet(db);
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(), "<html><body>\n</body></html>\n");
    }

    @Test
    public void testFull() throws IOException, SQLException {
        GetProductsServlet servlet = new GetProductsServlet(db);
        fillDB();
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "<html><body>\n" +
                        "apple\t1</br>\n" +
                        "xiaomi\t2</br>\n" +
                        "</body></html>\n");
    }
}