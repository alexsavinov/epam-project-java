package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Search.SearchGetCommand;
import com.itermit.railway.command.Search.SearchPostCommand;
import com.itermit.railway.command.Search.SearchResetCommand;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Route;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Search Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * SearchResetCommand, SearchGetCommand, SearchGetCommandFail0,
 * SearchPostCommand, SearchPostCommandFail0.
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
        RouteManager.class,
        StationManager.class,
        Transport.class
})
public class SearchCommandTest {

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
    RouteManager routeManager;
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

        PowerMockito.mockStatic(RouteManager.class);
        routeManager = PowerMockito.mock(RouteManager.class);
        when(RouteManager.getInstance()).thenReturn(routeManager);

        mockLogger = mock(Logger.class);
    }

    /**
     * SearchResetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void SearchResetCommandTest() throws Exception {

        Whitebox.setInternalState(SearchResetCommand.class, "logger", mockLogger);

        String result = new SearchResetCommand().execute(request, response);
        Assertions.assertEquals(null, result);
    }

    /**
     * SearchGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void SearchGetCommandTest() throws Exception {

        Whitebox.setInternalState(SearchGetCommand.class, "logger", mockLogger);

        when(session.getAttribute("daterange")).thenReturn(null);

        String result = new SearchGetCommand().execute(request, response);
        SearchGetCommand.getDefaultDaterange();

        Assertions.assertEquals("/search.jsp", result);
    }

    /**
     * SearchGetCommand - Fail #0 - DBException - stationManager.getAll
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void SearchGetCommandTestFail0Test() throws Exception {

        Whitebox.setInternalState(SearchGetCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(stationManager).getAll();

        String result = new SearchGetCommand().execute(request, response);
        Assertions.assertEquals("/stations", result);
    }

    /**
     * SearchPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void SearchPostCommandTest() throws Exception {

        Whitebox.setInternalState(SearchPostCommand.class, "logger", mockLogger);

        Paginator paginator = PowerMockito.mock(Paginator.class);
        when(routeManager.getPaginated(any(QueryMaker.class))).thenReturn(paginator);
        when(paginator.getData()).thenReturn(new ArrayList<Route>());

        when(request.getParameter("daterange")).thenReturn("09-09-2022 12:00:00 09-09-2022 15:00:00");
        when(request.getParameter("train_number")).thenReturn("A11");
        when(request.getParameter("cost_min")).thenReturn("12");
        when(request.getParameter("cost_max")).thenReturn("12");
        when(request.getParameter("travel_time_min")).thenReturn("4");
        when(request.getParameter("travel_time_max")).thenReturn("15");
        when(request.getParameter("seats_available_min")).thenReturn("3");
        when(request.getParameter("seats_available_max")).thenReturn("55");

        when(request.getParameter("sort_train_number")).thenReturn("sort_train_number");
        when(request.getParameter("sort_station_departure")).thenReturn("sort_station_departure");
        when(request.getParameter("sort_date_departure")).thenReturn("sort_date_departure");
        when(request.getParameter("sort_station_arrival")).thenReturn("sort_station_arrival");
        when(request.getParameter("sort_date_arrival")).thenReturn("sort_date_arrival");
        when(request.getParameter("sort_travel_time")).thenReturn("sort_travel_time");
        when(request.getParameter("sort_travel_cost")).thenReturn("sort_travel_cost");
        when(request.getParameter("sort_seats_available")).thenReturn("sort_seats_available");
        when(request.getParameter("sort_seats_reserved")).thenReturn("sort_seats_reserved");
        when(request.getParameter("sort_seats_total")).thenReturn("sort_seats_total");

        String result = new SearchPostCommand().execute(request, response);
        Assertions.assertEquals("/search", result);
    }

    /**
     * SearchPostCommand - Fail #0 - DBException - stationManager.getAll
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void SearchPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(SearchPostCommand.class, "logger", mockLogger);

        Paginator paginator = PowerMockito.mock(Paginator.class);
        when(routeManager.getPaginated(any(QueryMaker.class))).thenReturn(paginator);
        when(paginator.getData()).thenReturn(new ArrayList<Route>());

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(stationManager).getAll();

        new SearchPostCommand().execute(request, response);
    }

}


