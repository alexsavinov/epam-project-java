package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Maintenances Station CRUD operations.
 * <p>
 * Uses dbManager.
 * Handles SQLException and returns DBException.
 *
 * @author O.Savinov
 */
public class StationManager {

    private static StationManager instance;
    private static final Logger logger = LogManager.getLogger(StationManager.class);
    private DBManager dbManager;

    private StationManager() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of StationManager.
     *
     * @return StationManager
     */
    public static synchronized StationManager getInstance() {
        if (instance == null) {
            instance = new StationManager();
        }
        return instance;
    }

    /**
     * Returns a list of Stations.
     *
     * @return ArrayList of Stations
     * @throws DBException
     */
    public ArrayList<Station> getAll() throws DBException {

        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return StationDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get stations list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a Station by id.
     *
     * @param id Integer value of Station id
     * @return Station
     * @throws DBException
     */
    public Station get(int id) throws DBException {

        logger.debug("#get(station).");

        try (Connection connection = dbManager.getConnection()) {
            return StationDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Adds a new Station.
     *
     * @param station Station to add
     * @throws DBException
     */
    public void add(Station station) throws DBException {

        logger.debug("#add(station).");

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().add(connection, station);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while adding station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Deletes existed Station by id.
     *
     * @param id Integer value of Station id
     * @throws DBException
     */
    public void delete(int id) throws DBException {

        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Updates existed Station.
     *
     * @param id         Integer value of Station id
     * @param station       Station data to update
     * @throws DBException
     */
    public void update(int id, Station station) throws DBException {

        logger.debug("#update(id, station): {} -- {}", id, station);

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().update(connection, id, station);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

}
