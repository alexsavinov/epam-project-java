package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().add(connection, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while adding user!", e);
            throw new DBException(errorMessage, e);
        }
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

    public void  update(int id, User user) throws DBException {
        logger.debug("#update(id, user): {} -- {}", id, user);

        try (Connection connection = dbManager.getConnection()) {
            UserDAOImpl.getInstance().update(connection, id, user);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting user!", e);
            throw new DBException(errorMessage, e);
        }
    }

//    public void createUsers(User... users) throws DBException {
//        Connection con = null;
//        try {
//            con = dbManager.getConnection();
//            con.setTransactionIsolation(
//                    Connection.TRANSACTION_READ_COMMITTED);
//            con.setAutoCommit(false);
//
//            for (User user : users) {
//                dbManager.createUser(con, user);
//            }
//
//            con.commit();
//        } catch (SQLException ex) {
//            // (1) write to log: log.error("Cannot create users", ex);
//            ex.printStackTrace();
//
//            // (2)
//            if (con != null) {
//                try {
//                    con.rollback();
//                } catch (SQLException e) {
//                    // write to log
//                    e.printStackTrace();
//                }
//            }
//
//            // (3) throw your own exception
//            throw new DBException("Cannot create users", ex);
//        }
//    }
//
//    public List<User> findAllUsers() throws DBException {
//        try (Connection con = dbManager.getConnection()) {
//            return dbManager.findAllUsers(con);
//        } catch (SQLException ex) {
//            // (1) write to log: log.error(..., ex);
//            ex.printStackTrace();
//
//            // (2) throw your own exception
//            throw new DBException("Cannot find all users", ex);
//        }
//    }

}
