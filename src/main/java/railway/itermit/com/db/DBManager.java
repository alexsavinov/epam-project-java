package railway.itermit.com.db;

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
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    /*
     * Returns an instance of DB Manager
     */
    public static synchronized DBManager getInstance() {

        logger.trace("#DBManager instance created.");

        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {
    }

    /*
     * Creates connection to DB
     */
    public Connection getConnection() throws DBException {

        logger.trace("#getConnection.");

        Connection connection;
        DataSource ds;

        try {
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("jdbc/sql-connector-pool");
        } catch (NamingException e) {
            logger.error("NamingException while getConnection(): {}", e.getMessage());
            throw new DBException("NamingException while getConnection()!", e);
        }

        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            logger.error("SQLException while getConnection(): {}", e.getMessage());
            throw new DBException("SQLException while getConnection()!", e);
        }

        logger.warn("connection: {}", connection);

        return connection;
    }

    /*
     * Closes Connection to DB
     */
    public static void closeConnection(Connection connection) throws DBException {

        logger.trace("#closeConnection(connection).");

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("SQLException while closeConnection(connection): {}", e.getMessage());
                throw new DBException("SQLException while closeConnection(connection)!", e);
            }
        }
    }

    /*
     * Closes Statement to DB
     */
    public static void closeStatement(Statement statement) throws DBException {

        logger.trace("#closeStatement(statement).");

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("SQLException while closeStatement(statement): {}", e.getMessage());
                throw new DBException("SQLException while closeStatement(statement)!", e);
            }
        }
    }

    /*
     * Closes preparedStatement
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws DBException {

        logger.trace("#closePreparedStatement(preparedStatement).");

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error("SQLException while closePreparedStatement(preparedStatement): {}", e.getMessage());
                throw new DBException("SQLException while closePreparedStatement(preparedStatement)!", e);
            }
        }
    }

    /*
     * Closes resultSet
     */
    public static void closeResultSet(ResultSet resultSet) throws DBException {

        logger.trace("#closeResultSet(resultSet).");

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error("SQLException while closeResultSet(resultSet): {}", e.getMessage());
                throw new DBException("SQLException while closeResultSet(resultSet)!", e);
            }
        }
    }

    /*
     * Closes resultSet
     */
    public static void rollback(Connection connection) throws DBException {

        logger.trace("#rollback(connection).");

        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("SQLException while rollback(connection): {}", e.getMessage());
                throw new DBException("SQLException while rollback(connection)!", e);
            }
        }

    }

}
