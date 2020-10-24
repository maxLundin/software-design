package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddProductServletTest extends DBClass {
    @Test
    public void testEmpty() throws IOException {
        AddProductServlet servlet = new AddProductServlet();
        Assertions.assertThrows(Exception.class, () -> servlet.doGet(request, response));
    }

    @Test
    public void testFull() throws IOException, SQLException {
        AddProductServlet servlet = new AddProductServlet();
        doSql("insert into PRODUCT (name, price) values " +
                "('apple', 1), " +
                "('xiaomi', 2)");
        when(request.getParameter("name")).thenReturn("apple");
        when(request.getParameter("price")).thenReturn("1");
        servlet.doGet(request, response);
        Assertions.assertEquals(status.my, HttpServletResponse.SC_OK);
        Assertions.assertEquals(pw.toString(),
                "OK\n");
    }

}

