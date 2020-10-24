package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
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

public class BaseTest {

    protected final StringWriter pw = new StringWriter();

    protected static class MyInt {
        int my;

        public MyInt() {
            my = 0;
        }

        public MyInt(int c) {
            my = c;
        }
    }

    @Mock
    protected HttpServletRequest request;
    @Mock
    protected HttpServletResponse response;

    protected final MyInt status = new MyInt();

    protected void doSql(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @BeforeEach
    protected void createDB() throws SQLException {
        doSql("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @BeforeEach
    protected void setup() throws IOException {
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
    protected void deleteDB() throws SQLException {
        doSql("DROP TABLE IF EXISTS PRODUCT");
    }

}
