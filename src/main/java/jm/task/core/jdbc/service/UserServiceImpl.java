package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service

public class UserServiceImpl implements UserService {
    UserDao userDaoJDBCImpl = new UserDaoJDBCImpl();

    public UserServiceImpl() throws IOException {
    }

    @Override
    public void createUsersTable() throws SQLException {
        userDaoJDBCImpl.createUsersTable();
    }

    public void dropUsersTable() throws SQLException {
        userDaoJDBCImpl.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        userDaoJDBCImpl.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) throws SQLException {
        userDaoJDBCImpl.removeUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDaoJDBCImpl.getAllUsers();
    }

    public void cleanUsersTable() throws SQLException {
        userDaoJDBCImpl.cleanUsersTable();
    }
}
