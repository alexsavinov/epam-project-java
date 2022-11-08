package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Station.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Station;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Station Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * StationsListCommand, StationsListCommandFail0, StationAddGetCommand,
 * StationAddPostCommand, StationAddPostCommandFail0, StationDeleteCommand,
 * StationDeleteCommandFail0, StationEditGetCommandFail0, StationEditGetCommand,
 * StationEditPostCommandFail0. *
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
        StationManager.class,
        Transport.class
})
public class StationCommandTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    StringWriter stringWriter;
    @Mock
    PrintWriter writer;
    @Mock
    StationManager stationManager;
    @Mock
    Logger mockLogger;

    @BeforeClass
    public static void oneTimeSetup() {
        System.setProperty("log4j.defaultInitOverride", Boolean.toString(true));
        System.setProperty("log4j.ignoreTCL", Boolean.toString(true));
    }

    @Before
    public void setUp() throws IOException, SQLException {

        PowerMockito.mockStatic(Transport.class);

        PowerMockito.mockStatic(Session.class);
        when(request.getSession()).thenReturn(session);

        PowerMockito.mockStatic(PropertiesLoader.class);
        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        PowerMockito.mockStatic(StationManager.class);
        stationManager = PowerMockito.mock(StationManager.class);
        when(StationManager.getInstance()).thenReturn(stationManager);

        mockLogger = mock(Logger.class);
    }

    /**
     * StationsListCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationsListCommandTest() throws Exception {

        Whitebox.setInternalState(StationsListCommand.class, "logger", mockLogger);

        String result = new StationsListCommand().execute(request, response);
        Assertions.assertEquals("/stations.jsp", result);
    }

    /**
     * StationsListCommand - Fail #0 - DBException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void StationsListCommandFail0Test() throws Exception {

        Whitebox.setInternalState(StationsListCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null)).when(stationManager).getAll();

        new StationsListCommand().execute(request, response);
    }

    /**
     * StationAddGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationAddGetCommandTest() throws Exception {

        Whitebox.setInternalState(StationAddGetCommand.class, "logger", mockLogger);

        String result = new StationAddGetCommand().execute(request, response);
        Assertions.assertEquals("/station.jsp", result);
    }

    /**
     * StationAddPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationAddPostCommandTest() throws Exception {

        Whitebox.setInternalState(StationAddPostCommand.class, "logger", mockLogger);

        String result = new StationAddPostCommand().execute(request, response);
        Assertions.assertEquals("/stations", result);
    }

    /**
     * StationAddPostCommand - Fail #0 - DBException - stationManager.add
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void StationAddPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(StationAddPostCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(stationManager).add(any(Station.class));

        String result = new StationAddPostCommand().execute(request, response);
        Assertions.assertEquals("/stations", result);
    }

    /**
     * StationDeleteCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationDeleteCommandTest() throws Exception {

        Whitebox.setInternalState(StationDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new StationDeleteCommand().execute(request, response);
        Assertions.assertEquals("/stations", result);
    }

    /**
     * StationDeleteCommand - Fail #0 - DBException - stationManager.delete
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void StationDeleteCommandFail0Test() throws Exception {

        Whitebox.setInternalState(StationDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(stationManager).delete(anyInt());

        String result = new StationDeleteCommand().execute(request, response);
        Assertions.assertEquals("/stations", result);
    }

    /**
     * StationEditGetCommand - Fail #0 - DBException - stationManager.delete
     *
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void StationEditGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(StationEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);

        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);
        when(stationManager.get(1)).thenThrow(new DBException("DBException", null));

        String result = new StationEditGetCommand().execute(request, response);
        Assertions.assertEquals("/station.jsp", result);
    }

    /**
     * StationEditGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationEditGetCommandTest() throws Exception {

        Whitebox.setInternalState(StationEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new StationEditGetCommand().execute(request, response);
        Assertions.assertEquals("/station.jsp", result);
    }

    /**
     * StationEditPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void StationEditPostCommandTest() throws Exception {

        Whitebox.setInternalState(StationEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new StationEditPostCommand().execute(request, response);
        Assertions.assertEquals("/stations/edit/1", result);
    }

    /**
     * StationEditPostCommand - Fail #0 - DBException - stationManager.update
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void StationEditPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(StationEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(stationManager).update(anyInt(), any(Station.class));

        String result = new StationEditPostCommand().execute(request, response);
        Assertions.assertEquals("/stations/edit/1", result);
    }

}


