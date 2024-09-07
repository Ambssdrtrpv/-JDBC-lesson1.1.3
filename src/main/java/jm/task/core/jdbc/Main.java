package jm.task.core.jdbc;


import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private final static UserService userService;

    static {
        try {
            userService = new UserServiceImpl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        userService.createUsersTable();

        userService.saveUser("Джо", "Байден", (byte) 78);
        userService.saveUser("Трамп", "Дональд", (byte) 74);
        userService.saveUser("Барак", "Обама", (byte) 59);
        userService.saveUser("Джордж", "Буш", (byte) 74);

        userService.removeUserById(2);

        userService.getAllUsers();
    }
}
