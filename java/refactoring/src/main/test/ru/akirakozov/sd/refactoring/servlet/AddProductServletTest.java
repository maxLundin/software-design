package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

class AddProductServletTest extends BaseTest {
    @Test
    public void testEmpty() {
        AddProductServlet servlet = new AddProductServlet(db);
        Assertions.assertNotEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertThrows(Exception.class, () -> servlet.doGet(request, response));
    }

    @Test
    public void testFull() throws IOException, SQLException {
        AddProductServlet servlet = new AddProductServlet(db);
        fillDB();
        when(request.getParameter("name")).thenReturn("apple");
        when(request.getParameter("price")).thenReturn("1");
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "OK\n");
    }

}

