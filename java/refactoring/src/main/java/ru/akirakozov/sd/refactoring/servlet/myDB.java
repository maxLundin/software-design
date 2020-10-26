package ru.akirakozov.sd.refactoring.servlet;

import java.sql.*;

public class myDB {

    private final String database;

    public myDB(String database) {
        this.database = database;
    }

    public String getDB() {
        return database;
    }

    public void createDB() throws SQLException {
        execDBUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    public void addElem(String name, String price) throws SQLException {
        execDBUpdate("INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")");
    }

    public void execDBUpdate(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }


}
