package com.itermit.railway;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.PropertiesLoader;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * DAO Implementation - Tests.
 * <p>
 * Includes test for: OrderDAOImpl, RouteDAOImpl, StationDAOImpl, UserDAOImpl.
 *
 * @author O.Savinov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        AuthLoginCommand.class,
        CommandContainer.class,
        DBManager.class,
        LoggerFactory.class,
        PropertiesLoader.class,
        Session.class,
        OrderManager.class,
        RouteManager.class,
        StationManager.class,
        UserManager.class,
        OrderDAOImpl.class,
        RouteDAOImpl.class,
        StationDAOImpl.class,
        Transport.class
})
public class DAOImplTest {

    @Mock
    StationManager stationManager;
    @Mock
    UserManager userManager;
    @Mock
    RouteManager routeManager;
    @Mock
    OrderManager orderManager;
    @Mock
    Logger mockLogger;

    @BeforeClass
    public static void oneTimeSetup() {
        System.setProperty("log4j.defaultInitOverride", Boolean.toString(true));
        System.setProperty("log4j.ignoreTCL", Boolean.toString(true));
    }

    @Before
    public void setUp() throws IOException, SQLException {

        PowerMockito.mockStatic(PropertiesLoader.class);
        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);

        PowerMockito.mockStatic(StationManager.class);
        stationManager = PowerMockito.mock(StationManager.class);
        when(StationManager.getInstance()).thenReturn(stationManager);

        PowerMockito.mockStatic(UserManager.class);
        userManager = PowerMockito.mock(UserManager.class);
        when(UserManager.getInstance()).thenReturn(userManager);

        PowerMockito.mockStatic(OrderManager.class);
        orderManager = PowerMockito.mock(OrderManager.class);
        when(OrderManager.getInstance()).thenReturn(orderManager);

        PowerMockito.mockStatic(RouteManager.class);
        routeManager = PowerMockito.mock(RouteManager.class);
        when(RouteManager.getInstance()).thenReturn(routeManager);

        mockLogger = mock(Logger.class);
    }

    /**
     * OrderDAOImpl Test
     *
     * @throws Exception
     */
    @Test
    public void OrderDAOImplTest() throws Exception {

        Whitebox.setInternalState(OrderDAOImpl.class, "logger", mockLogger);

        Connection connection = PowerMockito.mock(Connection.class);

        PowerMockito.mockStatic(DBManager.class);
        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(DBManager.getInstance()).thenReturn(dbManager);

        OrderDAOImpl orderDAO = OrderDAOImpl.getInstance();

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        Order orderTest = new Order.Builder()
                .withUser(new User.Builder().build())
                .withRoute(new Route.Builder()
                        .withStationDeparture(new Station.Builder().build())
                        .withStationArrival(new Station.Builder().build())
                        .build())
                .withSeats(0)
                .withDateReserve(null)
                .build();

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(orderTest);

        /* getAll */
        Assertions.assertEquals(orders.size(), orderDAO.getAll(connection).size());

        /* getPaginated */
        QueryMaker queryMaker = new QueryMaker.Builder()
                .withMainQuery("11")
                .withCondition("route_id", Condition.EQ, "1")
                .withSort("route_id", Condition.ASC)
                .build();
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        ArrayList<Order> result = (ArrayList<Order>) orderDAO.getPaginated(connection, queryMaker).getData();
        Assertions.assertEquals(orders.size(), result.size());

        /* getFiltered */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        result = orderDAO.getFiltered(connection, queryMaker);
        Assertions.assertEquals(orders.size(), result.size());

        /* getGroupedByRouteOfUser */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        result = orderDAO.getGroupedByRouteOfUser(connection, 1);
        Assertions.assertEquals(orders.size(), result.size());

        /* get */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Order orderResult = orderDAO.get(connection, 1);
        Assertions.assertEquals(orderTest.toString(), orderResult.toString());

        /* add */
        orderDAO.add(connection, orderTest);
        /* update */
        orderDAO.update(connection, 1, orderTest);
        /* delete */
        orderDAO.delete(connection, 1);
    }

    /**
     * RouteDAOImpl Test
     *
     * @throws Exception
     */
    @Test
    public void RouteDAOImplTest() throws Exception {

        Whitebox.setInternalState(RouteDAOImpl.class, "logger", mockLogger);

        Connection connection = PowerMockito.mock(Connection.class);

        PowerMockito.mockStatic(DBManager.class);
        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(DBManager.getInstance()).thenReturn(dbManager);

        RouteDAOImpl routeDAO = RouteDAOImpl.getInstance();

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        Route routeTest = new Route.Builder()
                .withStationDeparture(new Station.Builder().build())
                .withStationArrival(new Station.Builder().build())
                .build();

        ArrayList<Route> routes = new ArrayList<>();
        routes.add(routeTest);

        /* getAll */
        Assertions.assertEquals(routes.size(), routeDAO.getAll(connection).size());

        /* getTotalRows */
        QueryMaker queryMaker = new QueryMaker.Builder()
                .withMainQuery("11")
                .withCondition("route_id", Condition.EQ, "1")
                .withSort("route_id", Condition.ASC)
                .build();
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Assertions.assertEquals(0, routeDAO.getTotalRows(connection, queryMaker));

        /* getPaginated */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        ArrayList<Order> result = (ArrayList<Order>) routeDAO.getPaginated(connection, queryMaker).getData();
        Assertions.assertEquals(routes.size(), result.size());

        /* get */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Route routeResult = routeDAO.get(connection, 1);
        Assertions.assertEquals(routeTest.toString(), routeResult.toString());

        /* add */
        routeDAO.add(connection, routeTest);
        /* update */
        routeDAO.update(connection, 1, routeTest);
        /* delete */
        routeDAO.delete(connection, 1);
    }

    /**
     * StationDAOImpl Test
     *
     * @throws Exception
     */
    @Test
    public void StationDAOImplTest() throws Exception {

        Whitebox.setInternalState(StationDAOImpl.class, "logger", mockLogger);

        Connection connection = PowerMockito.mock(Connection.class);

        PowerMockito.mockStatic(DBManager.class);
        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(DBManager.getInstance()).thenReturn(dbManager);

        StationDAOImpl stationDAO = StationDAOImpl.getInstance();

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        Station stationTest = new Station.Builder().build();

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(stationTest);

        /* getAll */
        Assertions.assertEquals(stations.size(), stationDAO.getAll(connection).size());

        /* get */
        QueryMaker queryMaker = new QueryMaker.Builder()
                .withMainQuery("11")
                .withCondition("station_id", Condition.EQ, "1")
                .withSort("station_id", Condition.ASC)
                .build();
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Station stationResult = stationDAO.get(connection, 1);
        Assertions.assertEquals(stationTest.toString(), stationResult.toString());

        /* getFiltered */
        Assertions.assertEquals(null, stationDAO.getFiltered(connection, queryMaker));
        /* getPaginated */
        Assertions.assertEquals(null, stationDAO.getPaginated(connection, queryMaker));
        /* add */
        stationDAO.add(connection, stationTest);
        /* update */
        stationDAO.update(connection, 1, stationTest);
        /* delete */
        stationDAO.delete(connection, 1);
    }

    /**
     * UserDAOImpl Test
     *
     * @throws Exception
     */
    @Test
    public void UserDAOImplTest() throws Exception {

        Whitebox.setInternalState(UserDAOImpl.class, "logger", mockLogger);

        Connection connection = PowerMockito.mock(Connection.class);

        PowerMockito.mockStatic(DBManager.class);
        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(DBManager.getInstance()).thenReturn(dbManager);

        UserDAOImpl userDAO = UserDAOImpl.getInstance();

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        User userTest = new User.Builder().build();

        ArrayList<User> users = new ArrayList<>();
        users.add(userTest);

        /* getAll */
        Assertions.assertEquals(users.size(), userDAO.getAll(connection).size());

        /* get */
        QueryMaker queryMaker = new QueryMaker.Builder()
                .withMainQuery("11")
                .withCondition("user_id", Condition.EQ, "1")
                .withSort("user_id", Condition.ASC)
                .build();
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        User userResult = userDAO.get(connection, 1);
        Assertions.assertEquals(userTest.toString(), userResult.toString());

        User userResult1 = userDAO.get(connection, userTest);
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Assertions.assertEquals(userTest.toString(), userResult1.toString());

        /* getFiltered */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Assertions.assertEquals(users, userDAO.getFiltered(connection, queryMaker));

        /* getTotalRows */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Assertions.assertEquals(0, userDAO.getTotalRows(connection, queryMaker));

        /* getPaginated */
        PowerMockito.when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        Paginator paginatorTest = new Paginator.Builder().withData(users).build();

        Assertions.assertEquals(((ArrayList<User>) paginatorTest.getData()).size(),
                ((ArrayList<User>) userDAO.getPaginated(connection, queryMaker).getData()).size());

        /* add */
        userDAO.add(connection, userTest);
        /* update */
        userDAO.update(connection, 1, userTest);
        /* delete */
        userDAO.delete(connection, 2);
    }

}


