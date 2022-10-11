package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.UserDAO;
import com.itermit.railway.dao.entity.User;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    private static final String SQL_GET_ALL_USERS = "select id, name, password, isadmin from users";
    private static final String SQL_GET_USER_BY_CREDENTIALS = "select id, name, password, isadmin from users where name = ? and password = ?";
    private static final String SQL_GET_USER_BY_ID = "select id, name, password, isadmin from users where id = ?";
    private static final String SQL_ADD_USER = "INSERT INTO users (name, password, isadmin) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE users set name = ?, password = ?, isadmin = ? where id = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";

    @Override
    public User get(int id) throws DBException {

        logger.trace("#get(id): {}", id);

        User user = null;

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .withPassword(resultSet.getString(User.F_PASSWORD))
                        .withIsAdmin(resultSet.getBoolean(User.F_ISADMIN))
                        .build();
            }
        } catch (SQLException e) {
            logger.error("SQLException while get(id): {}", e.getMessage());
            throw new DBException("SQLException while get(id)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User get(User user) throws DBException {

        logger.trace("#get(user): {}", user);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_CREDENTIALS);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .withPassword(resultSet.getString(User.F_PASSWORD))
                        .withIsAdmin(resultSet.getBoolean(User.F_ISADMIN))
                        .build();
            }

        } catch (SQLException e) {
            logger.error("SQLException while get(user): {}", e.getMessage());
            throw new DBException("SQLException while get(user)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

        return user;
    }

    @Override
    public ArrayList<User> getAll() throws DBException {

        logger.trace("#getAll()");

        ArrayList<User> users = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .withPassword(resultSet.getString(User.F_PASSWORD))
                        .withIsAdmin(resultSet.getBoolean(User.F_ISADMIN))
                        .build());
            }

        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("SQLException while getAll()!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

        return users;
    }

    @Override
    public void add(User user) throws DBException {

        logger.trace("#add(user): {}", user);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_USER);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(user): {}", e.getMessage());
            throw new DBException("SQLException while add(user)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

    }

    @Override
    public void update(int id, User user) throws DBException {

        logger.trace("#update(id, user): {} -- {}", id, user);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);

            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException while update(id, user): {}", e.getMessage());
            throw new DBException("SQLException while update(id, user)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

    }

    @Override
    public void delete(int id) throws DBException {

        logger.trace("#delete(id): {}", id);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while delete(id): {}", e.getMessage());
            throw new DBException("SQLException while delete(id)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

    }

}
