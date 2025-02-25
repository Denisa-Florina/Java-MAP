package src.lab7.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectionn {
    private Connection connection;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "denisa");
        }
        catch (SQLException e) {}
        return null;
    }
}
