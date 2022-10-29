package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.itermit.railway.dao.impl.OrderDAOImpl.*;
import static com.itermit.railway.dao.impl.RouteDAOImpl.*;
import static com.itermit.railway.db.Fields.*;

/**
 * Maintenances Order CRUD operations.
 * <p>
 * Uses dbManager.
 * Handles SQLException and returns DBException.
 *
 * @author O.Savinov
 */
public class OrderManager {

    private static OrderManager instance;
    private static final Logger logger = LogManager.getLogger(OrderManager.class);
    private DBManager dbManager;

    private OrderManager() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of OrderManager.
     *
     * @return OrderManager
     */
    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Returns a list of Orders.
     *
     * @return ArrayList of Orders
     * @throws DBException
     */
    public ArrayList<Order> getAll() throws DBException {

        logger.debug("#getAll().");

        try (Connection connection = dbManager.getConnection()) {
            return OrderDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting orders list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a Order by id.
     *
     * @param id Integer value of Order id
     * @return Order
     * @throws DBException
     */
    public Order get(int id) throws DBException {

        logger.debug("#get(id).");

        try (Connection connection = dbManager.getConnection()) {
            return OrderDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting order!", e);
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Adds a new Order.
     *
     * @param order Order to add
     * @throws DBException
     */
    public void add(Order order) throws DBException {

        logger.debug("#add(order).");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();

            /* Set transaction isolation */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            /* Get back reserved Seats to Route */
            preparedStatement = connection.prepareStatement(SQL_ADD_RESERVE_ROUTE);
            int l = 0;
            preparedStatement.setInt(++l, order.getSeats());
            preparedStatement.setInt(++l, order.getRoute().getId());
            logger.trace(preparedStatement);
            preparedStatement.executeUpdate();

            /* Check if reserved Seats in Route remains less than Total seats */
            preparedStatement = connection.prepareStatement(SQL_CHECK_RESERVE_ROUTE);
            preparedStatement.setInt(1, order.getRoute().getId());
            logger.trace(preparedStatement);
            ResultSet resultSetRoute = preparedStatement.executeQuery();
            if (resultSetRoute.next()) {
                /* Rollback if not */
                DBManager.rollback(connection);
                logger.error("Total seats exceeded for Route (id): {}", order.getRoute().getId());
                throw new DBException("Total seats exceeded for Route (id): {}" + order.getRoute().getId(), null);
            }

            OrderDAOImpl.getInstance().add(connection, order);

            /* Commit */
            connection.commit();

        } catch (SQLException e) {
            /* Rollback if error */
            try {
                DBManager.rollback(connection);
                String errorMessage = CommandContainer.getErrorMessage("Error while adding order!", e);
                logger.error("{} {}", errorMessage, e.getMessage());
                throw new DBException(errorMessage, e);
            } catch (SQLException ex) {
                logger.error("Error rollback connection! {}", ex.getMessage());
                throw new DBException("Error rollback connection!", ex);
            }
        } finally {
            try {
                DBManager.closeConnection(connection);
                DBManager.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                logger.error("Error closing Prepared statement! {}", e.getMessage());
                throw new DBException("Error closing Prepared statement!", e);
            }
        }
    }

    /**
     * Updates existed Order.
     *
     * @param id    Integer value of Order id
     * @param order Order data to update
     * @throws DBException
     */
    public void update(int id, Order order) throws DBException {

        logger.debug("#update(id, order): {} -- {}", id, order);

        try (Connection connection = dbManager.getConnection()) {
            OrderDAOImpl.getInstance().update(connection, id, order);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while updating order!", e);
            logger.error("{} {}", errorMessage, e.getMessage());
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Deletes existed Order by id.
     *
     * @param id Integer value of Order id
     * @throws DBException
     */
    public void delete(int id) throws DBException {

        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            OrderDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting order!", e);
            logger.error("{} {}", errorMessage, e.getMessage());
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a grouped by Route list of Orders for related User.
     *
     * @param userId Integer value of User id
     * @return ArrayList of Orders grouped by Route
     * @throws DBException
     */
    public ArrayList<Order> getGroupedByRouteOfUser(int userId) throws DBException {

        logger.debug("#getGroupedByRouteOfUser(userId). {}", userId);

        try (Connection connection = dbManager.getConnection()) {
            return OrderDAOImpl.getInstance().getGroupedByRouteOfUser(connection, userId);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting orders, " +
                    "grouped by Route (for current user)!", e);
            logger.error("{} {}", errorMessage, e.getMessage());
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns wrapped by Paginator list of Orders.
     *
     * @param queryMaker QueryMaker to construct SQL-query with conditions
     * @return Paginated list of Orders
     * @throws DBException
     */
    public Paginator getPaginated(QueryMaker queryMaker) throws DBException {

        logger.debug("#getPaginated(queryMaker).");

        try (Connection connection = dbManager.getConnection()) {
            return OrderDAOImpl.getInstance().getPaginated(connection, queryMaker);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting Orders!", e);
            logger.error("{} {}", errorMessage, e.getMessage());
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Returns a filtered list of Orders.
     *
     * @param queryMaker QueryMaker to construct SQL-query with conditions
     * @return ArrayList of Orders
     * @throws DBException
     */
    public ArrayList<Order> getFiltered(QueryMaker queryMaker) throws DBException {

        logger.debug("#getFiltered(queryMaker).");

        try (Connection connection = dbManager.getConnection()) {
            return OrderDAOImpl.getInstance().getFiltered(connection, queryMaker);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting Orders!", e);
            logger.error("{} {}", errorMessage, e.getMessage());
            throw new DBException(errorMessage, e);
        }
    }

    /**
     * Deletes exact number of seats from Order by Order id.
     * <p>
     * Also checks if reserved Seats in Route remains above zero.
     * And checks if Seats in Order remains above zero.
     *
     * @param id    Integer value of Order id
     * @param seats Integer value of seats to delete
     * @throws DBException
     */
    public void deleteReserve(int id, int seats) throws DBException {

        logger.debug("#deleteReserve(id, seats). {} {}", id, seats);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();

            /* Set transaction isolation */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            /* Get Order to remove reserved Seats later */
            preparedStatement = connection.prepareStatement(SQL_GET_ORDER_BY_ID);
            preparedStatement.setInt(1, id);
            logger.trace(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            int routeSeats;
            int route_id = 0;

            if (resultSet.next()) {
                route_id = resultSet.getInt(ORDER_ROUTE_ID);
                routeSeats = resultSet.getInt(ORDER_SEATS);
            } else {
                /* Rollback */
                connection.rollback();
                logger.error("Error deleting reserved Seats. Cannot get Order by id: {}", route_id);
                throw new DBException("Error deleting reserved Seats. Cannot get Order by id: " + route_id, null);
            }

            /* Remove reserved Seats from Route */
            preparedStatement = connection.prepareStatement(SQL_REMOVE_RESERVE_ROUTE);
            int l = 0;
            preparedStatement.setInt(++l, seats);
            preparedStatement.setInt(++l, route_id);
            logger.trace(preparedStatement);
            preparedStatement.executeUpdate();

            /* Check if reserved Seats in Route remains above zero */
            preparedStatement = connection.prepareStatement(SQL_CHECK_RESERVE_ROUTE);
            preparedStatement.setInt(1, route_id);
            logger.trace(preparedStatement);
            ResultSet resultSetRoute = preparedStatement.executeQuery();
            if (resultSetRoute.next()) {
                /* Rollback */
                connection.rollback();
                logger.error("Error deleting reserved Seats. " +
                        "Not enough reserve seats to remove from Route (id): {}", route_id);
                throw new DBException("Error deleting reserved Seats. " +
                        "Not enough reserve seats to remove from Route (id): " + route_id, null);
            }

            if (seats == routeSeats) {
                /* Seats to remove equals to Total seats in Order - deleting whole Order */
                OrderDAOImpl.getInstance().delete(connection, id);
            } else {
                /* Seats to remove less than Total seats in Order - decreasing Seats in Order */
                preparedStatement = connection.prepareStatement(SQL_REMOVE_RESERVE_ORDER);
                l = 0;
                preparedStatement.setInt(++l, seats);
                preparedStatement.setInt(++l, id);
                preparedStatement.executeUpdate();

                /* Check if Seats in Order remains above zero */
                preparedStatement = connection.prepareStatement(SQL_CHECK_RESERVE_ORDER);
                preparedStatement.setInt(1, id);
                ResultSet resultSetOrder = preparedStatement.executeQuery();
                if (resultSetOrder.next()) {
                    /* Rollback */
                    connection.rollback();
                    logger.error("Not enough reserved Seats to remove from Order (id): {}", id);
                    throw new DBException("Not enough reserved Seats to remove from Order (id): " + id, null);
                }
            }
            /* Commit */
            connection.commit();

        } catch (SQLException e) {
            /* Rollback if error */
            try {
                DBManager.rollback(connection);
                String errorMessage = CommandContainer.getErrorMessage("Error while deleting Reserve!", e);
                logger.error("{} {}", errorMessage, e.getMessage());
                throw new DBException(errorMessage, e);
            } catch (SQLException ex) {
                logger.error("Error rollback connection! {}", ex.getMessage());
                throw new DBException("Error rollback connection!", ex);
            }
        } finally {
            try {
                DBManager.closeConnection(connection);
                DBManager.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                logger.error("Error closing Prepared statement! {}", e.getMessage());
                throw new DBException("Error closing Prepared statement!", e);
            }
        }
    }

}
