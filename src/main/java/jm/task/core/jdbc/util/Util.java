package jm.task.core.jdbc.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class Util {

    @Bean
    public static Connection getConnection() {
        String DB_URL = "jdbc:postgresql://localhost:5433/forKata";
        String USER = "postgres";
        String PASS = "delta1501d";

      Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
