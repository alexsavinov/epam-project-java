package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.StationDAO;
import com.itermit.railway.db.entity.Station;
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

import static com.itermit.railway.db.Fields.ENTITY_ID;
import static com.itermit.railway.db.Fields.USER_NAME;

/**
 * DAO Implementation for Stations.
 * <p>
 * Processes request to database from OrderManager with a given connection.
 *
 * @author O.Savinov
 */
public class StationDAOImpl implements StationDAO {

    private DBManager dbManager;
    private static StationDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(StationDAOImpl.class);
    /* SQL Queries */
    private static final String SQL_GET_ALL_STATIONS = "" +
            "SELECT id, name " +
            "FROM stations";
    private static final String SQL_GET_STATIONS_BY_ID = "" +
            "SELECT id, name " +
            "FROM stations " +
            "WHERE id = ?";
    private static final String SQL_ADD_STATION = "" +
            "INSERT INTO stations (name) " +
            "VALUES (?)";
    private static final String SQL_UPDATE_STATION = "" +
            "UPDATE stations " +
            "SET name = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_STATION = "" +
            "DELETE FROM stations " +
            "WHERE id = ?";

    /**
     * Default constructor
     */
    private StationDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of Station's DAO implementation.
     *
     * @return StationDAOImpl
     */
    public static synchronized StationDAOImpl getInstance() {
        if (instance == null) {
            instance = new StationDAOImpl();
        }
        return instance;
    }

    /**
     * Returns a list of Stations.
     *
     * @param connection Connection to DataSource
     * @return ArrayList of Stations
     * @throws SQLException
     */
    @Override
    public ArrayList<Station> getAll(Connection connection) throws SQLException {

        logger.debug("#getAll(connection).");

        ArrayList<Station> stations = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
//            logger.trace(SQL_GET_ALL_STATIONS);
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_STATIONS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                stations.add(extract(resultSet));
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return stations;
    }

    /**
     * Returns wrapped by Paginator list of Stations.
     * <p>
     * Actually always returns null. Persists for backward compatibility.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return Paginated list of Stations
     * @throws SQLException
     */
    @Override
    public Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException {
        return null;
    }

    /**
     * Returns a filtered list of Stations.
     * <p>
     * Actually always returns null. Persists for backward compatibility.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return ArrayList of Stations
     * @throws SQLException
     */
    @Override
    public ArrayList<Station> getFiltered(Connection connection, QueryMaker query) throws SQLException {
        return null;
    }

    /**
     * Returns a Station by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Station id
     * @return Station
     * @throws SQLException
     */
    @Override
    public Station get(Connection connection, int id) throws SQLException {

        logger.debug("#get(connection, id): {}", id);

        Station station = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            logger.trace(SQL_GET_STATIONS_BY_ID);
            preparedStatement = connection.prepareStatement(SQL_GET_STATIONS_BY_ID);
            int l = 0;
            preparedStatement.setInt(++l, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                station = extract(resultSet);
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return station;
    }

    /**
     * Adds a new Station.
     *
     * @param connection Connection to DataSource
     * @param station    Station to add
     * @throws SQLException
     */
    @Override
    public void add(Connection connection, Station station) throws SQLException {

        logger.debug("#add(connection, station): {}", station);

        PreparedStatement preparedStatement = null;
        try {
            logger.trace(SQL_UPDATE_STATION);
            preparedStatement = connection.prepareStatement(SQL_ADD_STATION);
            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Updates existed Station.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Station id
     * @param station    Station data to update
     * @throws SQLException
     */
    @Override
    public void update(Connection connection, int id, Station station) throws SQLException {

        logger.debug("#update(connection, id, station): {} -- {}", id, station);

        PreparedStatement preparedStatement = null;
        try {
            logger.trace(SQL_UPDATE_STATION);
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATION);
            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Deletes existed Station by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Station id
     * @throws SQLException
     */
    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(connection, id): {}", id);

        PreparedStatement preparedStatement = null;
        try {
            logger.trace(SQL_DELETE_STATION);
            preparedStatement = connection.prepareStatement(SQL_DELETE_STATION);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Extracts Station entity from given ResultSet.
     *
     * @param resultSet ResultSet to process
     * @return Station
     * @throws SQLException
     */
    @Override
    public Station extract(ResultSet resultSet) throws SQLException {

        return new Station.Builder()
                .withId(resultSet.getInt(ENTITY_ID))
                .withName(resultSet.getString(USER_NAME))
                .build();
    }

}
