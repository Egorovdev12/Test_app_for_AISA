package egorovIVSolution.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnection{
    public static Connection connection;
    public static Statement statement;

    static {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:15432/coffee_logs_n78",
                    "postgres", "808080");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}