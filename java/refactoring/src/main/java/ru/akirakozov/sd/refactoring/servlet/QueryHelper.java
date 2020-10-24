package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class QueryHelper {

    public static class IllegalCommandException extends IllegalArgumentException {
        IllegalCommandException(String str) {
            super(str);
        }
    }

    static private void printStatement(PrintWriter pw, String str) {
        if (!str.isEmpty()) {
            pw.println(str);
        }
    }

    static private String getUnknown(String command) {
        return "Unknown command: " + command;
    }

    static private String getQuery(String command) {
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
            case "getAll":
                return "SELECT * FROM PRODUCT";
            default:
                throw new IllegalCommandException(getUnknown(command));
        }
    }

    static private String getHeader(String command) {
        if (command == null) {
            throw new IllegalCommandException(getUnknown(null));
        }
        switch (command) {
            case "max":
                return "<h1>Product with max price: </h1>";
            case "min":
                return "<h1>Product with min price: </h1>";
            case "sum":
                return "Summary price: ";
            case "count":
                return "Number of products: ";
            case "getAll":
                return "";
            default:
                throw new IllegalCommandException(getUnknown(command));
        }
    }

    static private String queryResultToText(String command, ResultSet rs) throws SQLException {
        if (command == null) {
            throw new IllegalCommandException(getUnknown(null));
        }
        switch (command) {
            case "max":
            case "min":
            case "getAll":
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    if (!first) {
                        sb.append("\n");
                    }
                    sb.append(name).append("\t").append(price).append("</br>");
                    first = false;
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

    static public void doGet(HttpServletResponse response, final String command) throws IOException {
        PrintWriter pw = response.getWriter();
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();

                ResultSet rs = stmt.executeQuery(QueryHelper.getQuery(command));
                printStatement(pw, "<html><body>");
                printStatement(pw, QueryHelper.getHeader(command));
                printStatement(pw, QueryHelper.queryResultToText(command, rs));
                printStatement(pw, "</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (QueryHelper.IllegalCommandException e) {
            printStatement(pw, e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
