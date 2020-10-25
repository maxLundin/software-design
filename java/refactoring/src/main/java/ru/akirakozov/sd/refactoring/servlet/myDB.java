package ru.akirakozov.sd.refactoring.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class myDB {
    private final String database;

    public myDB(String database) {
        this.database = database;
    }

    public String getDB() {
        return database;
    }

    public void createDB() throws SQLException {
        execDBupdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    public void execDBupdate(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }
}
