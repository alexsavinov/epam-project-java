package com.itermit.railway.Managers;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.PropertiesLoader;
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
 * User Manager tests.
 * <p>
 * Includes tests for:
 * <p>
 * UserManager.
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
        UserDAOImpl.class,
        Transport.class
})
public class UserManagerTest {

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
     * UserManager - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserManagerTest() throws Exception {

        Whitebox.setInternalState(UserManager.class, "logger", mockLogger);

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

        PowerMockito.mockStatic(UserDAOImpl.class);
        UserDAOImpl userDAO = PowerMockito.mock(UserDAOImpl.class);

        when(UserDAOImpl.getInstance()).thenReturn(userDAO);

        Connection connection = PowerMockito.mock(Connection.class);

        DBManager dbManagerTest = DBManager.getInstance();
        when(dbManagerTest.getConnection()).thenReturn(connection);

        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        when(userDAO.getAll(connection)).thenReturn(new ArrayList<>());

        UserManager userManagerTests = UserManager.getInstance();

        Assertions.assertEquals(new ArrayList<User>(), userManagerTests.getAll());

        Assertions.assertEquals(null, userManagerTests.get(1));

        Assertions.assertEquals(null, userManagerTests.get(new User.Builder()
                .withId(211)
                .withName("User")
                .build()));

        when(resultSet.next()).thenReturn(Boolean.FALSE, Boolean.FALSE);
        userManagerTests.add(new User.Builder()
                .withId(211)
                .withName("User")
                .build());

        userManagerTests.update(126, new User.Builder()
                .withId(28)
                .withName("User3")
                .build());

        userManagerTests.delete(131);
    }

}
