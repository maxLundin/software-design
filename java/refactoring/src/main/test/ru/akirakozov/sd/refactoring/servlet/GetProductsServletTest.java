package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    }


    @AfterEach
    public void deleteDB() throws SQLException {
        doSql("DROP TABLE IF EXISTS PRODUCT");
    }

    @Test
    public void test() throws IOException {
        GetProductsServlet servlet = new GetProductsServlet();
        servlet.doGet(request, response);
        System.out.println(pw.toString());
    }
}