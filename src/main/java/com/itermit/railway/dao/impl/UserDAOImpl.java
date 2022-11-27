package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.UserDAO;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.itermit.railway.db.Fields.*;

/**
 * DAO Implementation for Users.
 * <p>
 * Processes request to database from OrderManager with a given connection.
 *
 * @author O.Savinov
 */
public class UserDAOImpl implements UserDAO {

    private DBManager dbManager;
    private static UserDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    /* SQL Queries */
    public static final String SQL_GET_ALL_USERS = "" +
            "SELECT id, name, password, email, isadmin, isactive, activation_token " +
            "FROM users";
    private static final String SQL_GET_USER_BY_CREDENTIALS = "" +
            "SELECT * " +
            "FROM users " +
//            "WHERE name = ? AND password = SHA2(?, 0)";
            "WHERE name = ?";
    private static final String SQL_GET_USER_BY_TOKEN = "" +
            "SELECT * " +
            "FROM users " +
            "WHERE activation_token = ?";
    public static final String SQL_GET_USER_BY_ID = "" +
            "SELECT * " +
            "FROM users " +
            "WHERE id = ?";
    private static final String SQL_ADD_USER = "" +
            "INSERT INTO users (name, password, email, isadmin, isactive, activation_token) " +
            "VALUES (?, SHA2(?, 0), ?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER = "" +
            "UPDATE users " +
//            "SET name = ?, email = ? , isadmin = ? , isactive = ? , activation_token = ? " +
            "SET name = ?, email = ? , password = SHA2(?, 0), isadmin = ? , isactive = ? , activation_token = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_USER = "" +
            "DELETE FROM users " +
            "WHERE id = ?";
    private static final String SQL_GET_TOTAL_ROWS = "" +
            "SELECT COUNT(*) total_rows " +
            "FROM users";

    /**
     * Default constructor
     */
    private UserDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of User's DAO implementation.
     *
     * @return UserDAOImpl
     */
    public static synchronized UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    /**
     * Returns a list of Users.
     *
     * @param connection Connection to DataSource
     * @return ArrayList of Users
     * @throws SQLException
     */
    @Override
    public ArrayList<User> getAll(Connection connection) throws SQLException {

        logger.debug("#getAll(connection).");

        ArrayList<User> users = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(extract(resultSet));
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return users;
    }

    /**
     * Returns a list of Users.
     *
     * @param connection Connection to DataSource
     * @return ArrayList of Users
     * @throws SQLException
     */
    @Override
    public Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException {

        logger.debug("#getPaginated(connection, query).");

        ArrayList<User> users = getFiltered(connection, query);

        query.setQueryMain(SQL_GET_TOTAL_ROWS);
        query.deleteQueryOffset();

        int totalRows = getTotalRows(connection, query);
        int total_pages = (int) Math.ceil(totalRows / (float) (Paginator.PAGE_SIZE));
        return new Paginator.Builder()
                .withPage(query.getPage())
                .withPages(total_pages)
                .withResults(totalRows)
                .withData(users)
                .build();
    }

    /**
     * Returns total number of records in DB table for given query.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @throws SQLException
     */
    public int getTotalRows(Connection connection, QueryMaker query) throws SQLException {

        int totalRows = 0;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = query.getPreparedStatement(connection);

            ResultSet rsTotalRows = preparedStatement.executeQuery();
            if (rsTotalRows.next()) {
                totalRows = rsTotalRows.getInt(TOTAL_ROWS);
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return totalRows;
    }

    /**
     * Returns a filtered list of Users.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return ArrayList of Users
     * @throws SQLException
     */
    @Override
    public ArrayList<User> getFiltered(Connection connection, QueryMaker query) throws SQLException {

        logger.debug("#getFiltered(connection, query).  {}", query);

        ArrayList<User> users = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            query.setQueryMain(SQL_GET_ALL_USERS);
            preparedStatement = query.getPreparedStatement(connection);
            logger.info("preparedStatement: {}", preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(extract(resultSet));
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return users;
    }

    /**
     * Returns a User by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of User id
     * @return User
     * @throws SQLException
     */
    @Override
    public User get(Connection connection, int id) throws SQLException {

        logger.debug("#get(connection, id): {}", id);

        User user = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extract(resultSet);
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    /**
     * Returns a User by entity data.
     *
     * @param connection Connection to DataSource
     * @param user       User entity
     * @return User
     * @throws SQLException
     */
    @Override
    public User get(Connection connection, User user) throws SQLException {

        logger.debug("#get(connection, user): {}", user);

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_CREDENTIALS);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
//            preparedStatement.setString(++l, user.getPassword());
            logger.trace(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extract(resultSet);
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    /**
     * Returns a User by token.
     *
     * @param connection Connection to DataSource
     * @param token      Activation token string
     * @return User
     * @throws SQLException
     */
    public User getByToken(Connection connection, String token) throws SQLException {

        logger.debug("#getByToken(connection, token): {}", token);

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        User user = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_TOKEN);
            int l = 0;
            preparedStatement.setString(++l, token);
            logger.trace(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extract(resultSet);
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    /**
     * Adds a new User.
     *
     * @param connection Connection to DataSource
     * @param user       User to add
     * @throws SQLException
     */
    @Override
    public void add(Connection connection, User user) throws SQLException {

        logger.debug("#add(connection, user): {}", user);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_USER);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setString(++l, user.getEmail());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.setBoolean(++l, user.getIsActive());
            preparedStatement.setString(++l, user.getActivationToken());
//            logger.trace(preparedStatement);

            preparedStatement.executeUpdate();
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Updates existed User.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of User id
     * @param user       User data to update
     * @throws SQLException
     */
    @Override
    public void update(Connection connection, int id, User user) throws SQLException {

        logger.debug("#update(connection, id, user): {} -- {}", id, user);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
            preparedStatement.setString(++l, user.getEmail());
            preparedStatement.setBoolean(++l, user.getIsAdmin());
            preparedStatement.setBoolean(++l, user.getIsActive());
            preparedStatement.setString(++l, user.getActivationToken());
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
        }

    }

    /**
     * Deletes existed User by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of User id
     * @throws SQLException
     */
    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(id): {}", id);

        if (id == 1) {
            logger.error("Error while delete(id): cannot delete admin!");
            throw new SQLException("Error while deleting user: cannot delete admin!");
        }

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Extracts User entity from given ResultSet.
     *
     * @param resultSet ResultSet to process
     * @return User
     * @throws SQLException
     */
    @Override
    public User extract(ResultSet resultSet) throws SQLException {

        return new User.Builder()
                .withId(resultSet.getInt(ENTITY_ID))
                .withName(resultSet.getString(USER_NAME))
                .withPassword(resultSet.getString(USER_PASSWORD))
                .withEmail(resultSet.getString(USER_EMAIL))
                .withIsAdmin(resultSet.getBoolean(USER_IS_ADMIN))
                .withIsActive(resultSet.getBoolean(USER_IS_ACTIVE))
                .withActivationToken(resultSet.getString(USER_ACTIVATION_TOKEN))
                .build();
    }

}
