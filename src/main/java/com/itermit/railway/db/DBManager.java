package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Serves DB connection to DataSource.
 * <p>
 * Also, can close PreparedStatement and ResultSet, rollback connection.
 * Singleton.
 *
 * @author O.Savinov
 */
public class DBManager {

    private static DBManager instance;
    private final DataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    /**
     * Default constructor
     */
    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/sql-connector-pool");
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot obtain a data source", e);
        }
    }

    /**
     * Returns an instance of DB Manager.
     *
     * @return DBManager
     */
    public static synchronized DBManager getInstance() {

        if (instance == null) {
            logger.debug("#DBManager getInstance().");
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * Returns a DB connection from the Pool Connections.
     * <p>
     * Before using you have to configure DataSource and
     * Connections Pool in webapp/META-INF/context.xml file.
     *
     * @return DB connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {

        logger.trace("#getConnection().");

        Connection connection;
        connection = dataSource.getConnection();

        return connection;
    }

    /**
     * Closes Connection to DataSource.
     *
     * @param connection Connection
     * @throws SQLException
     */
    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            logger.trace("#closeConnection(connection).");
            connection.close();
        }
    }

    /**
     * Closes PreparedStatement.
     *
     * @param preparedStatement PreparedStatement to close.
     * @throws SQLException
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement != null) {
            logger.trace("#closePreparedStatement(preparedStatement).");
            preparedStatement.close();
        }
    }

    /**
     * Closes ResultSet.
     *
     * @param resultSet ResultSet to close.
     * @throws SQLException
     */
    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            logger.trace("#closeResultSet(resultSet).");
            resultSet.close();
        }
    }

    /**
     * Rollbacks a connection.
     *
     * @param connection Connection to rollback.
     * @throws SQLException
     */
    public static void rollback(Connection connection) throws SQLException {

        if (connection != null) {
            logger.trace("#rollback(connection).");
            connection.rollback();
        }
    }

}
