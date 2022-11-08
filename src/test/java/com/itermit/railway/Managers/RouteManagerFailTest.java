package com.itermit.railway.Managers;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
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
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Route Manager fail tests.
 * <p>
 * Includes tests for:
 * <p>
 * RouteManagerFail.
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
public class RouteManagerFailTest {

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
     * RouteManagerTest - Fail #0 - IllegalStateException - obtain DataSource
     *
     * @throws Exception
     */
    @Test
    public void RouteManagerFail0Test() throws Exception {

        /* logger */
        Whitebox.setInternalState(RouteManager.class, "logger", mockLogger);

        /* context */
        InitialContextFactoryBuilder contextFactoryBuilder = mock(InitialContextFactoryBuilder.class);
        InitialContextFactory contextFactory = mock(InitialContextFactory.class);
        Context context = mock(Context.class);
        NamingManager.setInitialContextFactoryBuilder(contextFactoryBuilder);
        when(contextFactoryBuilder.createInitialContextFactory(any(Hashtable.class))).thenReturn(contextFactory);
        when(contextFactory.getInitialContext(any(Hashtable.class))).thenReturn(context);

        /* dbManager */
        DBManager dbManager = PowerMockito.mock(DBManager.class);
        when(context.lookup("java:/comp/env")).thenReturn(context);

        /* dataSource */
        DataSource dataSource = PowerMockito.mock(DataSource.class);
        when(context.lookup("jdbc/sql-connector-pool")).thenReturn(dataSource);

        /* dbManager */
        when(DBManager.getInstance()).thenReturn(dbManager);

        /* dbManagerTest */
        DBManager dbManagerTest = DBManager.getInstance();
        Connection connection = PowerMockito.mock(Connection.class);
        when(dbManagerTest.getConnection()).thenReturn(connection);

        /* preparedStatement */
        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        /* routeManagerTests */
        RouteManager routeManagerTests = RouteManager.getInstance();
        when(preparedStatement.executeQuery()).thenThrow(new SQLException());
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

        Assertions.assertThrows(DBException.class, () -> routeManagerTests.getAll());
        Assertions.assertThrows(DBException.class, () -> routeManagerTests.getPaginated(new QueryMaker.Builder().build()));
        Assertions.assertThrows(DBException.class, () -> routeManagerTests.get(1));
        Assertions.assertThrows(DBException.class, () -> routeManagerTests.add(
                new Route.Builder()
                        .withTravelCost(211)
                        .withSeatsTotal(11)
                        .withStationDeparture(new Station.Builder().withId(21).build())
                        .withStationArrival(new Station.Builder().withId(22).build())
                        .build())
        );

        PowerMockito.when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        Assertions.assertThrows(DBException.class, () -> routeManagerTests.delete(1));

        Assertions.assertThrows(DBException.class, () ->
                routeManagerTests.update(11,
                        new Route.Builder()
                                .withTravelCost(211)
                                .withSeatsTotal(11)
                                .withStationDeparture(new Station.Builder().withId(21).build())
                                .withStationArrival(new Station.Builder().withId(22).build())
                                .build())
        );
    }
}


