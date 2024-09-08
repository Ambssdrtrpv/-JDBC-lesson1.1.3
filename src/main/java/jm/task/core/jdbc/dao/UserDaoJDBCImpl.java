package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

@Component
public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    {
        try {
            connection = getConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUsersTable() throws SQLException {

        try (Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);

            statement.execute("CREATE TABLE IF NOT EXISTS Users ("
                    + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "name varchar(100) not null,"
                    + "last_name varchar(100) not null,"
                    + "age smallint not null)");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() throws SQLException {
        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute("DROP TABLE IF EXISTS Users");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        long generatedId = 0;

        try(PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO Users (name, last_name, age) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setByte(3, age);

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                    }
                }
            }
            connection.commit();
        } catch (SQLException throwables) {
            connection.rollback();
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id=?")) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwable) {
            connection.rollback();
            throwable.printStackTrace();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> allUsers = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));

                allUsers.add(user);
            }
            connection.commit();
        } catch (SQLException throwables) {
            connection.rollback();
            throwables.printStackTrace();
        }
        return allUsers;
    }

    public void cleanUsersTable() throws SQLException {
        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute("TRUNCATE TABLE Users");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }
}
