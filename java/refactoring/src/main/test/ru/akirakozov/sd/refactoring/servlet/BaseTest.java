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
import java.sql.SQLException;

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

    protected final myDB db = new myDB("jdbc:sqlite:test.db");

    protected void doSql(String sql) throws SQLException {
        db.execDBupdate(sql);
    }

    @BeforeEach
    protected void createDB() throws SQLException {
        db.createDB();
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
