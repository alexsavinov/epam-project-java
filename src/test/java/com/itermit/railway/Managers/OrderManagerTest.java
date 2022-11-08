package com.itermit.railway.Managers;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
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
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Order Manager tests.
 * <p>
 * Includes tests for:
 * <p>
 * OrderManager.
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
public class OrderManagerTest {

    @Mock
    Logger mockLogger;

    @BeforeClass
    public static void oneTimeSetup() {
        System.setProperty("log4j.defaultInitOverride", Boolean.toString(true));
        System.setProperty("log4j.ignoreTCL", Boolean.toString(true));
    }

    @Before
    public void setUp() throws IOException, SQLException, NamingException {

        PowerMockito.mockStatic(PropertiesLoader.class);
        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);

        mockLogger = mock(Logger.class);
    }

    /**
     * OrderManager - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderManagerTest() throws Exception {

        Whitebox.setInternalState(OrderManager.class, "logger", mockLogger);

        InitialContextFactoryBuilder contextFactoryBuilder = mock(InitialContextFactoryBuilder.class);
        InitialContextFactory contextFactory = mock(InitialContextFactory.class);
        Context context = mock(Context.class);
        try {
            NamingManager.setInitialContextFactoryBuilder(contextFactoryBuilder);
            when(contextFactoryBuilder.createInitialContextFactory(any(Hashtable.class))).thenReturn(contextFactory);
            when(contextFactory.getInitialContext(any(Hashtable.class))).thenReturn(context);
        } catch (IllegalStateException e) {
        }

        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(context.lookup("java:/comp/env")).thenReturn(context);

        DataSource dataSource = PowerMockito.mock(DataSource.class);
        when(context.lookup("jdbc/sql-connector-pool")).thenReturn(dataSource);

        when(DBManager.getInstance()).thenReturn(dbManager);

        PowerMockito.mockStatic(OrderDAOImpl.class);
        OrderDAOImpl orderDAO = PowerMockito.mock(OrderDAOImpl.class);

        when(OrderDAOImpl.getInstance()).thenReturn(orderDAO);

        Connection connection = PowerMockito.mock(Connection.class);

        DBManager dbManagerTest = DBManager.getInstance();
        when(dbManagerTest.getConnection()).thenReturn(connection);

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        when(orderDAO.getAll(connection)).thenReturn(new ArrayList<>());

        OrderManager orderManagerTests = OrderManager.getInstance();

        Assertions.assertEquals(new ArrayList<Order>(), orderManagerTests.getAll());

        Assertions.assertEquals(new ArrayList<Order>(), orderManagerTests.getGroupedByRouteOfUser(1));

        Assertions.assertEquals(null, orderManagerTests.getPaginated(new QueryMaker.Builder().build()));

        Assertions.assertEquals(new ArrayList<Order>(), orderManagerTests.getFiltered(new QueryMaker.Builder().build()));

        Assertions.assertEquals(null, orderManagerTests.get(1));

        Assertions.assertThrows(DBException.class, () ->
                orderManagerTests.add(new Order.Builder()
                        .withSeats(11)
                        .withRoute(new Route.Builder().withId(21).build())
                        .build()));

        when(resultSet.next()).thenReturn(Boolean.FALSE, Boolean.FALSE);
        orderManagerTests.add(new Order.Builder()
                .withSeats(11)
                .withRoute(new Route.Builder().withId(21).build())
                .build());

        orderManagerTests.update(1, new Order.Builder()
                .withSeats(11)
                .withRoute(new Route.Builder().withId(21).build())
                .build());

        orderManagerTests.delete(1);

        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        orderManagerTests.deleteReserve(1, 12);

        Assertions.assertThrows(DBException.class, () -> orderManagerTests.deleteReserve(1, 12));

        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        Assertions.assertThrows(DBException.class, () -> orderManagerTests.deleteReserve(1, 12));
    }

}


