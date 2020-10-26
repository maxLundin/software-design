package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DB.myDB;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final myDB db;

    public QueryServlet(final myDB db) {
        this.db = db;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        QueryHelper.doGet(response, request.getParameter("command"), db);
    }

}
