package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

class QueryServletTest extends BaseTest {

    @Test
    void testEmpty() throws IOException {
        QueryServlet servlet = new QueryServlet();
        servlet.doGet(request, response);
        Assertions.assertEquals(pw.toString(), "Unknown command: null\n");
    }

    private void fillDB() throws SQLException {
        doSql("insert into PRODUCT (name, price) values " +
                "('apple', 1), " +
                "('xiaomi', 2)");
    }

    private void testTemplateCommand(String command) throws IOException {
        when(request.getParameter("command")).thenReturn(command);
        new QueryServlet().doGet(request, response);
    }

    @Test
    void testMax() throws IOException, SQLException {
        QueryServlet servlet = new QueryServlet();
        fillDB();
        testTemplateCommand("max");
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(), "<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "xiaomi\t2</br>\n" +
                "</body></html>\n");
    }

    @Test
    void testMin() throws IOException, SQLException {
        QueryServlet servlet = new QueryServlet();
        fillDB();
        testTemplateCommand("min");
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "apple\t1</br>\n" +
                        "</body></html>\n");
    }

    @Test
    void testSum() throws IOException, SQLException {
        QueryServlet servlet = new QueryServlet();
        fillDB();
        testTemplateCommand("sum");
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "<html><body>\n" +
                        "Summary price: \n" +
                        "3\n" +
                        "</body></html>\n");
    }

    @Test
    void testCount() throws IOException, SQLException {
        QueryServlet servlet = new QueryServlet();
        fillDB();
        testTemplateCommand("count");
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "<html><body>\n" +
                        "Number of products: \n" +
                        "2\n" +
                        "</body></html>\n");
    }

}