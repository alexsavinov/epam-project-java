package com.itermit.railway.Managers;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.*;
import com.itermit.railway.utils.PropertiesLoader;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Session;
import javax.mail.Transport;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * DB Manager fail tests.
 * <p>
 * Includes tests for:
 * <p>
 * DBManagerFail.
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
public class DBManagerFailTest {

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
     * DBManagerTest - Fail #0 - IllegalStateException - obtain DataSource
     *
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void DBManagerFailTest() throws Exception {

        PowerMockito.mockStatic(PropertiesLoader.class);
        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");

        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);
        InitialContextFactoryBuilder contextFactoryBuilder = mock(InitialContextFactoryBuilder.class);
        InitialContextFactory contextFactory = mock(InitialContextFactory.class);
        Context context = mock(Context.class);

        try {
            NamingManager.setInitialContextFactoryBuilder(contextFactoryBuilder);
            when(contextFactoryBuilder.createInitialContextFactory(any(Hashtable.class))).thenReturn(contextFactory);
            when(contextFactory.getInitialContext(any(Hashtable.class))).thenReturn(context);
        } catch (IllegalStateException e) {
        }

        when(context.lookup("java:/comp/env")).thenThrow(new NamingException());
        when(context.lookup("jdbc/sql-connector-pool")).thenThrow(new NamingException());

        DBManager.getInstance();

    }

}


