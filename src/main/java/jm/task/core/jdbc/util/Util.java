package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


@org.springframework.context.annotation.Configuration
public class Util {

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASS;
    static {
        try (FileReader reader = new FileReader("src/main/resources/application.properties")) {
            Properties props = new Properties();
            props.load(reader);
            DB_URL = props.getProperty("spring.datasource.url");
            DB_USER = props.getProperty("spring.datasource.username");
            DB_PASS = props.getProperty("spring.datasource.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) {
            try {
                Properties properties = new Properties();
                properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                properties.setProperty("hibernate.connection.url", DB_URL);
                properties.setProperty("hibernate.connection.username", DB_USER);
                properties.setProperty("hibernate.connection.password", DB_PASS);
                properties.setProperty("dialect", "org.hibernate.dialect.PostgreSQLDialect");

                Configuration configuration = new Configuration()
                        .addProperties(properties).addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sessionFactory;
    }

    @Bean
    public static Connection getConnection() throws IOException {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
