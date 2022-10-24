package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.itermit.railway.dao.impl.UserDAOImpl.SQL_GET_ALL_USERS;

public class UserManager {

    private static UserManager instance;
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private DBManager dbManager;

    private UserManager() {
        dbManager = DBManager.getInstance();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User get(int id) throws DBException {
        logger.debug("#get(user).");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get(user)!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public User get(User user) throws DBException {
        logger.debug("#get(user).");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().get(connection, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get(user)!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public ArrayList<User> getAll() throws DBException {
        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return UserDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get users list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void add(User user) throws DBException {
        logger.debug("#add(user).");

        Connection connection = null;

        try {
            connection = dbManager.getConnection();

            /* Set transaction isolation */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            if (checkSameName(connection, user)) {
                /* Rollback if not */
                DBManager.rollback(connection);
                logger.info("User with same name {} is already exists!", user.getName());
                throw new DBException("User with same name " + user.getName() + " is already exists!", null);
            }

            if (checkSameEmail(connection, user)) {
                /* Rollback if not */
                DBManager.rollback(connection);
                logger.info("User with same E-mail {} is already exists!", user.getEmail());
                throw new DBException("User with same E-mail " + user.getEmail() + " is already exists!", null);
            }

            UserDAOImpl.getInstance().add(connection, user);

            /* Commit */
            connection.commit();

        } catch (SQLException e) {
            /* Rollback if error */
            DBManager.rollback(connection);
            String errorMessage = CommandContainer.getErrorMessage("Error while adding user!", e);
            throw new DBException(errorMessage, e);
        } finally {
            try {
                DBManager.closeConnection(connection);
            } catch (SQLException e) {
                throw new DBException("Error closing Prepared statement! ", e);
            }
        }
    }

    public Boolean checkSameName(Connection connection, User user) throws SQLException {

        QueryMaker query = new QueryMaker.Builder()
                .withMainQuery(SQL_GET_ALL_USERS)
                .withCondition("name", Condition.EQ, user.getName())
                .build();
        ArrayList<User> users = UserDAOImpl.getInstance().getFiltered(connection, query);

        return users.size() > 0;
    }

    public Boolean checkSameEmail(Connection connection, User user) throws SQLException {

        QueryMaker query = new QueryMaker.Builder()
                .withMainQuery(SQL_GET_ALL_USERS)
                .withCondition("email", Condition.EQ, user.getEmail())
                .build();
        ArrayList<User> users = UserDAOImpl.getInstance().getFiltered(connection, query);

        return users.size() > 0;
    }

    public void delete(int id) throws DBException {
        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting user!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void update(int id, User user) throws DBException {
        logger.debug("#update(id, user): {} -- {}", id, user);

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().update(connection, id, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting user!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void updatePassword(int id, String password) throws DBException {
        logger.debug("#updatePassword(id, password): {}", id);

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().update(connection, id,
                    new User.Builder().withId(id).withPassword(password).build());
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while update user password!", e);
            throw new DBException(errorMessage, e);
        }
    }

//    public void activatePrepare(User user) throws DBException {
//        logger.debug("#activatePrepare(user): {} ", user);
//        try (Connection connection = dbManager.getConnection()) {
//            user.generateActivateToken();
//            user.setIsActive(false);
//            UserDAOImpl.getInstance().update(connection, user.getId(), user);
//        } catch (SQLException e) {
//            String errorMessage = CommandContainer.getErrorMessage("Error while prepare to activate user!", e);
//            throw new DBException(errorMessage, e);
//        }
//    }

}
