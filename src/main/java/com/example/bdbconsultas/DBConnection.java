package com.example.bdbconsultas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/BDBIEN",
                "pr",
                "pr_pass1");
    }
}
