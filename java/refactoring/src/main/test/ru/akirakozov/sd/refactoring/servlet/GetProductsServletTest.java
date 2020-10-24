package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.when;

class GetProductsServletTest {

    private final StringWriter pw = new StringWriter();

    private static class MyInt {
        int my;

        public MyInt() {
            my = 0;
        }

        public MyInt(int c) {
            my = c;
        }
    }

    private final MyInt status = new MyInt();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private void doSql(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @BeforeEach
    public void createDB() throws SQLException {
        doSql("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @BeforeEach
    private void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(pw));
        when(response.getWriter()).thenReturn(new PrintWriter(pw));
        when(response.getWriter()).thenReturn(new PrintWriter(pw));
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        Mockito.doAnswer(invocationOnMock -> {
            status.my = argument.getValue();
            return null;
        }).when(response).setStatus(argument.capture());
    }


    @AfterEach
    public void deleteDB() throws SQLException {
        doSql("DROP TABLE IF EXISTS PRODUCT");
    }

    @Test
    public void testEmpty() throws IOException {
        GetProductsServlet servlet = new GetProductsServlet();
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(), "<html><body>\n</body></html>\n");
    }

    @Test
    public void testFull() throws IOException, SQLException {
        GetProductsServlet servlet = new GetProductsServlet();
        doSql("insert into PRODUCT (name, price) values " +
                "('apple', 1), " +
                "('xiaomi', 2)");
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "<html><body>\n" +
                        "apple\t1</br>\n" +
                        "xiaomi\t2</br>\n" +
                        "</body></html>\n");
    }

}