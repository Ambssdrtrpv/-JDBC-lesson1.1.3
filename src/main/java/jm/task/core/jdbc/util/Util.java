package jm.task.core.jdbc.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.getProperty;

@Configuration
public class Util {

    @Bean
    public static Connection getConnection() throws IOException {
        FileReader reader = new FileReader("src/main/resources/application.properties");
        Properties properties = new Properties();
        properties.load(reader);
        String DB_URL = properties.getProperty("spring.datasource.url");
        String USER = properties.getProperty("spring.datasource.username");
        String PASS = properties.getProperty("spring.datasource.password");

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
