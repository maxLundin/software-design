package ru.akirakozov.sd.refactoring.DB;

import java.sql.*;

public interface DB {

    String getDBName();

    void createDB() throws SQLException;

    void addElem(String name, String price) throws SQLException;

    void execDBUpdate(String sql) throws SQLException;

}
