package com.itermit.railway.dao.impl;


import com.itermit.railway.dao.StationDAO;
import com.itermit.railway.dao.entity.Station;
import com.itermit.railway.dao.entity.User;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StationDAOImpl implements StationDAO {
    private static final Logger logger = LogManager.getLogger(StationDAOImpl.class);

    private static final String SQL_GET_ALL_STATIONS = "SELECT id, name FROM stations";
    private static final String SQL_GET_STATIONS_BY_ID = "SELECT id, name FROM stations WHERE id = ?";
    private static final String SQL_ADD_STATION = "INSERT INTO stations (name) VALUES (?)";
    private static final String SQL_UPDATE_STATION = "UPDATE stations SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_STATION = "DELETE FROM stations WHERE id = ?";


    @Override
    public ArrayList<Station> getAll() throws DBException {

        logger.trace("#getAll()");

        ArrayList<Station> stations = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_STATIONS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                stations.add(new Station.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .build());
            }

        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("SQLException while getAll()!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

        return stations;
    }

    @Override
    public Station get(int id) throws DBException {

        logger.trace("#get(id): {}", id);

        Station station = null;

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_STATIONS_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                station = new Station.Builder()
                        .withId(resultSet.getInt(Station.F_ID))
                        .withName(resultSet.getString(Station.F_NAME))
                        .build();
            }
        } catch (SQLException e) {
            logger.error("SQLException while get(id): {}", e.getMessage());
            throw new DBException("SQLException while get(id)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }

        return station;
    }

    @Override
    public void add(Station station) throws DBException {

        logger.trace("#add(station): {}", station);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_STATION);
            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(station): {}", e.getMessage());
            throw new DBException("SQLException while add(station)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

    @Override
    public void update(int id, Station station) throws DBException {

        logger.trace("#update(id, user): {} -- {}", id, station);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATION);

            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQLException while update(id, user): {}", e.getMessage());
            throw new DBException("SQLException while update(id, user)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

    @Override
    public void delete(int id) throws DBException {

        logger.trace("#delete(id): {}", id);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_STATION);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while update(id, user): {}", e.getMessage());
            throw new DBException("SQLException while update(id, user)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

}
