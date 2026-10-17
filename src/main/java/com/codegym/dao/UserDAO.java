
package com.codegym.dao;

import com.codegym.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/demo"
            + "?useSSL=false&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "password";

    private static final String INSERT_USER =
            "INSERT INTO users (name, email, country) VALUES (?, ?, ?)";

    private static final String SELECT_USER =
            "SELECT id, name, email, country FROM users WHERE id = ?";

    private static final String SELECT_ALL_USERS =
            "SELECT id, name, email, country FROM users ORDER BY id";

    private static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";

    private static final String UPDATE_USER =
            "UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?";

    private static final String SEARCH_BY_COUNTRY =
            "SELECT id, name, email, country FROM users "
            + "WHERE country LIKE ? ORDER BY id";

    private static final String SORT_BY_NAME =
            "SELECT id, name, email, country FROM users "
            + "ORDER BY name ASC";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD
        );
    }

    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_USER)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());

            statement.executeUpdate();
        }
    }

    @Override
    public User selectUser(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_USER)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("country")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_USERS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("country")
                ));
            }
        }

        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_USER)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_USER)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // Tìm kiếm người dùng theo quốc gia
    public List<User> searchByCountry(String country) throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SEARCH_BY_COUNTRY)) {

            statement.setString(1, "%" + country + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("country")
                    ));
                }
            }
        }

        return users;
    }

    // Sắp xếp người dùng theo tên A-Z
    public List<User> sortByName() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SORT_BY_NAME);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("country")
                ));
            }
        }

        return users;
    }
}
