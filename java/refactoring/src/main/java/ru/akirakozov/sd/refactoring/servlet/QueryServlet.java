package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    static class IllegalCommandException extends IllegalArgumentException {
        IllegalCommandException(String str) {
            super(str);
        }
    }

    private String getUnknown(String command) {
        return "Unknown command: " + command;
    }

    private String getQuery(String command) {
        if (command == null) {
            throw new IllegalCommandException(getUnknown(null));
        }
        switch (command) {
            case "max":
                return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            case "min":
                return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            case "sum":
                return "SELECT SUM(price) FROM PRODUCT";
            case "count":
                return "SELECT COUNT(*) FROM PRODUCT";
            default:
                throw new IllegalCommandException(getUnknown(command));
        }
    }

    private String getHeader(String command) {
        switch (command) {
            case "max":
                return "<h1>Product with max price: </h1>";
            case "min":
                return "<h1>Product with min price: </h1>";
            case "sum":
                return "Summary price: ";
            case "count":
                return "Number of products: ";
            default:
                throw new IllegalCommandException(getUnknown(command));
        }
    }

    private String queryResultToText(String command, ResultSet rs) throws SQLException {
        switch (command) {
            case "max":
            case "min":
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    sb.append(name).append("\t").append(price).append("</br>");
                }
                return sb.toString();
            case "sum":
            case "count":
                if (rs.next()) {
                    return rs.getInt(1) + "";
                } else {
                    return "";
                }
            default:
                throw new IllegalCommandException(getUnknown(command));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();

                ResultSet rs = stmt.executeQuery(getQuery(command));
                response.getWriter().println("<html><body>");
                response.getWriter().println(getHeader(command));
                response.getWriter().println(queryResultToText(command, rs));
                response.getWriter().println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (IllegalCommandException e) {
            response.getWriter().println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
