package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Maintenances Route CRUD operations.
 * <p>
 * Uses dbManager.
 * Handles SQLException and returns DBException.
 *
 * @author O.Savinov
 */
public class RouteManager {

    private static RouteManager instance;
    private static final Logger logger = LogManager.getLogger(RouteManager.class);
    private DBManager dbManager;

    private RouteManager() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of RouteManager.
     *
     * @return RouteManager
     */
    public static synchronized RouteManager getInstance() {
        if (instance == null) {
            instance = new RouteManager();
        }
        return instance;
    }

    /**
     * Returns a list of Routes.
     *
     * @return ArrayList of Routes
     * @throws DBException
     */
    public ArrayList<Route> getAll() throws DBException {

        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get routes list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a Route by id.
     *
     * @param id Integer value of Route id
     * @return Route
     * @throws DBException
     */
    public Route get(int id) throws DBException {

        logger.debug("#get(route).");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Adds a new Route.
     *
     * @param route Route to add
     * @throws DBException
     */
    public void add(Route route) throws DBException {

        logger.debug("#add(route).");

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().add(connection, route);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while adding route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Deletes existed Route by id.
     *
     * @param id Integer value of Route id
     * @throws DBException
     */
    public void delete(int id) throws DBException {

        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Updates existed Route.
     *
     * @param id    Integer value of Route id
     * @param route Route data to update
     * @throws DBException
     */
    public void update(int id, Route route) throws DBException {

        logger.debug("#update(id, route): {} -- {}", id, route);

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().update(connection, id, route);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while updating route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns wrapped by Paginator list of Routes.
     *
     * @param queryMaker QueryMaker to construct SQL-query with conditions
     * @return Paginated list of Routes
     * @throws DBException
     */
    public Paginator getPaginated(QueryMaker queryMaker) throws DBException {

        logger.debug("#getPaginated(queryMaker).");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().getPaginated(connection, queryMaker);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting paginated routes!", e);
            throw new DBException(errorMessage, e);
        }
    }
}
