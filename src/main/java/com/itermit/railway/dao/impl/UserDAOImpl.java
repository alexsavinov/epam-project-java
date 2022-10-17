package com.itermit.railway.dao.impl;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.UserDAO;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class UserDAOImpl implements UserDAO {

    private DBManager dbManager;
    private static UserDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    private static final String SQL_GET_ALL_USERS = "select id, name, password, isadmin from users";
    private static final String SQL_GET_USER_BY_CREDENTIALS = "select id, name, password, isadmin from users where name = ? and password = ?";
    private static final String SQL_GET_USER_BY_ID = "select id, name, password, isadmin from users where id = ?";
    private static final String SQL_ADD_USER = "INSERT INTO users (name, password, isadmin) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE users set name = ?, password = ?, isadmin = ? where id = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";

    public static synchronized UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private UserDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public User get(int id) throws DBException {

        logger.debug("#get(id): {}", id);

        User user = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
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
            String errorMessage = CommandContainer.getErrorMessage("Cannot find user.", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    @Override
    public User get(User user) throws DBException {

        logger.debug("#get(user): {}", user);

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
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
            String errorMessage = CommandContainer.getErrorMessage("SQLException while get(user)!", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    @Override
    public ArrayList<User> getAll() throws DBException {

        logger.debug("#getAll()");

        ArrayList<User> users = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
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
            String errorMessage = CommandContainer.getErrorMessage("SQLException while getAll()!", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return users;
    }

    @Override
    public void add(User user) throws DBException {

        logger.debug("#add(user): {}", user);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_ADD_USER);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("SQLException while add(user)!", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }

    }

    @Override
    public void update(int id, User user) throws DBException {

        logger.debug("#update(id, user): {} -- {}", id, user);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);

            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("SQLException while update(id, user)!", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }

    }

    @Override
    public void delete(int id) throws DBException {

        logger.debug("#delete(id): {}", id);

        if (id == 1) {
            logger.error("SQLException while delete(id): cannot delete admin!");
            throw new DBException("SQLException while delete(id): cannot delete admin!", null);
        }

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("SQLException while delete(id)!", e);
            throw new DBException(errorMessage, e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }

    }

}
