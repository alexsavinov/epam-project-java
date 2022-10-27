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

public class UserDAOImpl implements UserDAO {

    private DBManager dbManager;
    private static UserDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    public static final String SQL_GET_ALL_USERS = "" +
            "SELECT id, name, password, email, isadmin, isactive, activation_token " +
            "FROM users";
    private static final String SQL_GET_USER_BY_CREDENTIALS = "" +
            "SELECT * " +
            "FROM users " +
            "WHERE name = ? AND password = SHA2(?, 0)";
    private static final String SQL_GET_USER_BY_ID = "" +
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

    public int getTotalRows(Connection connection, QueryMaker query) throws SQLException {

        int totalRows = 0;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = query.getPreparedStatement(connection);

            ResultSet rsTotalRows = preparedStatement.executeQuery();
            if (rsTotalRows.next()) {
                totalRows = rsTotalRows.getInt("total_rows");
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return totalRows;
    }

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
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return users;
    }

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
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

    @Override
    public User get(Connection connection, User user) throws SQLException {

        logger.debug("#get(connection, user): {}", user);

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_CREDENTIALS);
            int l = 0;
            preparedStatement.setString(++l, user.getName());
            preparedStatement.setString(++l, user.getPassword());
//            logger.trace(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extract(resultSet);
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return user;
    }

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
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return users;
    }

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
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

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
            DBManager.closePreparedStatement(preparedStatement);
        }

    }

    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(id): {}", id);

        if (id == 1) {
            logger.error("Error while delete(id): cannot delete admin!");
            throw new SQLException("Error while delete(id): cannot delete admin!");
        }

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public User extract(ResultSet resultSet) throws SQLException {

        return new User.Builder()
                .withId(resultSet.getInt(User.F_ID))
                .withName(resultSet.getString(User.F_NAME))
                .withPassword(resultSet.getString(User.F_PASSWORD))
                .withEmail(resultSet.getString(User.F_EMAIL))
                .withIsAdmin(resultSet.getBoolean(User.F_ISADMIN))
                .withIsActive(resultSet.getBoolean(User.F_ISACTIVE))
                .withActivationToken(resultSet.getString(User.F_ACTIVATION_TOKEN))
                .build();
    }

}
