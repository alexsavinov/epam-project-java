package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import static com.itermit.railway.dao.impl.UserDAOImpl.SQL_GET_ALL_USERS;

/**
 * Maintenances User CRUD operations.
 * <p>
 * Uses dbManager.
 * Handles SQLException and returns DBException.
 *
 * @author O.Savinov
 */
public class UserManager {

    private static UserManager instance;
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private DBManager dbManager;

    private UserManager() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of UserManager.
     *
     * @return UserManager
     */
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Returns a list of Users.
     *
     * @return ArrayList of Users
     * @throws DBException
     */
    public ArrayList<User> getAll() throws DBException {

        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get users list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a User by id.
     *
     * @param id Integer value of User id
     * @return User
     * @throws DBException
     */
    public User get(int id) throws DBException {

        logger.debug("#get(id).");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get(user)!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a User by entity data.
     *
     * @param user User entity
     * @return User
     * @throws DBException
     */
    public User get(User user) throws DBException {

        logger.debug("#get(user).");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().get(connection, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get(user)!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a User by token.
     *
     * @param token Activation token string
     * @return User
     * @throws DBException
     */
    public User activate(String token) throws DBException {

        logger.debug("#getByToken(user).");

        Connection connection = null;
        User user = null;

        try {
            connection = dbManager.getConnection();

            /* Set transaction isolation */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            /* Search user by token string */
            user = UserDAOImpl.getInstance().getByToken(connection, token);

            if (user == null) {
                dbManager.rollback(connection);
                logger.info("User not found by provided token!");
                throw new DBException("User not found by provided token!", null);
            }

            if (user.getIsActive()) {
                dbManager.rollback(connection);
                logger.info("User is already activated!");
                throw new DBException("User is already activated!", null);
            }

            user.setIsActive(true);

            UserManager.getInstance().update(user.getId(), user);

            /* Commit */
            connection.commit();

        } catch (SQLException e) {
            /* Rollback if error */
            try {
                dbManager.rollback(connection);
                String errorMessage = CommandContainer.getErrorMessage("Error while activating user!", e);
                logger.error("{} {}", errorMessage, e.getMessage());
                throw new DBException(errorMessage, e);
            } catch (SQLException ex) {
                logger.error("Error rollback connection! {}", ex.getMessage());
                throw new DBException("Error rollback connection!", ex);
            }
        } finally {
            try {
                dbManager.closeConnection(connection);
            } catch (SQLException e) {
                logger.error("Error closing Prepared statement! {}", e.getMessage());
                throw new DBException("Error closing Prepared statement!", e);
            }
        }

        return user;
    }

    /**
     * Adds a new User.
     * <p>
     * Also checks if user with the same name or email already exist.
     *
     * @param user User to add
     * @throws DBException
     */
    public void add(User user) throws DBException {

        logger.debug("#add(user).");

        Connection connection = null;

        try {
            connection = dbManager.getConnection();

            /* Set transaction isolation */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            if (checkSameName(connection, user)) {
                /* Rollback if the same name */
                dbManager.rollback(connection);
                logger.info("User with same name {} is already exists!", user.getName());
                throw new DBException("User with same name " + user.getName() + " is already exists!", null);
            }

            if (checkSameEmail(connection, user)) {
                /* Rollback if the same email */
                dbManager.rollback(connection);
                logger.info("User with same E-mail {} is already exists!", user.getEmail());
                throw new DBException("User with same E-mail " + user.getEmail() + " is already exists!", null);
            }

            UserDAOImpl.getInstance().add(connection, user);

            /* Commit */
            connection.commit();

        } catch (SQLException e) {
            /* Rollback if error */
            try {
                dbManager.rollback(connection);
                String errorMessage = CommandContainer.getErrorMessage("Error while adding user!", e);
                logger.error("{} {}", errorMessage, e.getMessage());
                throw new DBException(errorMessage, e);
            } catch (SQLException ex) {
                logger.error("Error rollback connection! {}", ex.getMessage());
                throw new DBException("Error rollback connection!", ex);
            }
        } finally {
            try {
                dbManager.closeConnection(connection);
            } catch (SQLException e) {
                logger.error("Error closing Prepared statement! {}", e.getMessage());
                throw new DBException("Error closing Prepared statement!", e);
            }
        }
    }

    /**
     * Returns true if users have same name.
     *
     * @param connection Connection to DataSource
     * @param user       User data to check in DB
     * @return Boolean
     * @throws SQLException
     */
    public Boolean checkSameName(Connection connection, User user) throws SQLException {

        QueryMaker query = new QueryMaker.Builder()
                .withMainQuery(SQL_GET_ALL_USERS)
                .withCondition("name", Condition.EQ, user.getName())
                .build();
        ArrayList<User> users = UserDAOImpl.getInstance().getFiltered(connection, query);

        return users.size() > 0;
    }

    /**
     * Returns true if users have same email.
     *
     * @param connection Connection to DataSource
     * @param user       User data to check in DB
     * @return Boolean
     * @throws SQLException
     */
    public Boolean checkSameEmail(Connection connection, User user) throws SQLException {

        QueryMaker query = new QueryMaker.Builder()
                .withMainQuery(SQL_GET_ALL_USERS)
                .withCondition("email", Condition.EQ, user.getEmail())
                .build();
        ArrayList<User> users = UserDAOImpl.getInstance().getFiltered(connection, query);

        return users.size() > 0;
    }

    /**
     * Deletes existed User by id.
     *
     * @param id Integer value of User id
     * @throws DBException
     */
    public void delete(int id) throws DBException {

        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting user!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Updates existed User.
     *
     * @param id         Integer value of User id
     * @param user       User data to update
     * @throws DBException
     */
    public void update(int id, User user) throws DBException {

        logger.debug("#update(id, user): {} -- {}", id, user);

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().update(connection, id, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while updating user!", e);
            throw new DBException(errorMessage, e);
        }
    }

}
