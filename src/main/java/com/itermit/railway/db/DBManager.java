package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/*
 * Serves DB connection routines.
 * Trows DBException.
 * Singleton pattern example.
 */
public class DBManager {

    private static DBManager instance;
    private final DataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    /*
     * Returns an instance of DB Manager
     */
    public static synchronized DBManager getInstance() {

//        logger.debug("#DBManager getInstance().");

        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/sql-connector-pool");
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot obtain a data source", e);
        }
    }

    /*
     * Creates connection to DB
     */
    public Connection getConnection() throws SQLException {

//        logger.trace("#getConnection().");

        Connection connection;

//        try {
        connection = dataSource.getConnection();
//            logger.warn("connection: {}", connection);
//        } catch (SQLException e) {
//            logger.error("Error while getConnection(): {}", e.getMessage());
//            throw new SQLException("Error while getConnection()!", e);
//        }

        return connection;
    }

    /*
     * Closes Connection to DB
     */
    public static void closeConnection(Connection connection) throws SQLException {

//        logger.trace("#closeConnection(connection).");

        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                logger.error("Error while closeConnection(connection): {}", e.getMessage());
//                throw new DBException("Error while closeConnection(connection)!", e);
//            }
        }
    }

    /*
     * Closes Statement to DB
     */
    public static void closeStatement(Statement statement) throws SQLException {

//        logger.trace("#closeStatement(statement).");

        if (statement != null) {
//            try {
            statement.close();
//            } catch (SQLException e) {
//                logger.error("Error while closeStatement(statement): {}", e.getMessage());
//                throw new DBException("Error while closeStatement(statement)!", e);
//            }
        }
    }

    /*
     * Closes preparedStatement
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {

//        logger.trace("#closePreparedStatement(preparedStatement).");

        if (preparedStatement != null) {
//            try {
            preparedStatement.close();
//            } catch (SQLException e) {
//                logger.error("Error while closePreparedStatement(preparedStatement): {}", e.getMessage());
//                throw new DBException("Error while closePreparedStatement(preparedStatement)!", e);
//            }
        }
    }

    /*
     * Closes resultSet
     */
    public static void closeResultSet(ResultSet resultSet) throws SQLException {

//        logger.trace("#closeResultSet(resultSet).");

        if (resultSet != null) {
//            try {
            resultSet.close();
//            } catch (SQLException e) {
//                logger.error("Error while closeResultSet(resultSet): {}", e.getMessage());
//                throw new DBException("Error while closeResultSet(resultSet)!", e);
//            }
        }
    }

    /*
     * Closes resultSet
     */
    public static void rollback(Connection connection) throws DBException {

//        logger.trace("#rollback(connection).");

        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Error while rollback(connection): {}", e.getMessage());
                throw new DBException("Error while rollback(connection)!", e);
            }
        }

    }

}
