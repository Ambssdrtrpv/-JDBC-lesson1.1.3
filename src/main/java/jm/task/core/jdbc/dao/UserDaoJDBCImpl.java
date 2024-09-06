package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

@Component
public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = getConnection();

    public void createUsersTable() {

        Statement statement = null;
        try {
            statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "name varchar(100) not null,"
                    + "last_name varchar(100) not null,"
                    + "age smallint not null)";

            statement.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String dropTableSQL = "DROP TABLE IF EXISTS Users";
            statement.execute(dropTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        long generatedId = 0;

        try {
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO Users (name, last_name, age) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setByte(3, age);

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                        //System.out.println("Generated ID: " + generatedId);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id=?");

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Users";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));

                allUsers.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String dropTableSQL = "TRUNCATE TABLE Users";
            statement.execute(dropTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
