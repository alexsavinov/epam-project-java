package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Route.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Order;
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
 * Route Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * RoutesListCommand, RoutesListCommandFail0, RouteAddPostCommand,
 * RouteAddPostCommandFail0, RouteDeleteCommand, RouteDeleteCommandFail0,
 * RouteEditGetCommand, RouteEditGetCommandFail0, RouteAddGetCommand,
 * RouteAddGetCommandFail0, RouteEditPostCommand, RouteEditPostCommandFail0,
 * RouteInfoCommand, RouteInfoCommandFail0, RouteInfoCommandFail1.
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
        Paginator.class,
        RouteManager.class,
        StationManager.class,
        UserManager.class,
        Transport.class
})
public class RouteCommandTest {

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
    OrderManager orderManager;
    @Mock
    UserManager userManager;
    @Mock
    RouteManager routeManager;
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

        PowerMockito.mockStatic(OrderManager.class);
        orderManager = PowerMockito.mock(OrderManager.class);
        when(OrderManager.getInstance()).thenReturn(orderManager);

        PowerMockito.mockStatic(UserManager.class);
        userManager = PowerMockito.mock(UserManager.class);
        when(UserManager.getInstance()).thenReturn(userManager);

        PowerMockito.mockStatic(RouteManager.class);
        routeManager = PowerMockito.mock(RouteManager.class);
        when(RouteManager.getInstance()).thenReturn(routeManager);

        PowerMockito.mockStatic(StationManager.class);
        stationManager = PowerMockito.mock(StationManager.class);
        when(StationManager.getInstance()).thenReturn(stationManager);

        mockLogger = mock(Logger.class);
    }

    /**
     * RoutesListCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RoutesListCommandTest() throws Exception {

        Whitebox.setInternalState(RoutesListCommand.class, "logger", mockLogger);

        String result = new RoutesListCommand().execute(request, response);
        Assertions.assertEquals("/routes.jsp", result);
    }

    /**
     * RoutesListCommand - Fail #0 - DBException routeManager.getAll
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RoutesListCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RoutesListCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null)).when(routeManager).getAll();

        new RoutesListCommand().execute(request, response);
    }

    /**
     * RouteAddPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteAddPostCommandTest() throws Exception {

        Whitebox.setInternalState(RouteAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("train_number")).thenReturn("A1");
        when(request.getParameter("station_departure")).thenReturn("2");
        when(session.getAttribute("station_arrival")).thenReturn("3");
        when(session.getAttribute("date_departure")).thenReturn("2222");
        when(session.getAttribute("date_arrival")).thenReturn("2221");
        when(session.getAttribute("travel_cost")).thenReturn("33");
        when(session.getAttribute("seats_reserved")).thenReturn("21");
        when(session.getAttribute("seats_total")).thenReturn("12");

        String result = new RouteAddPostCommand().execute(request, response);
        Assertions.assertEquals("/routes", result);
    }

    /**
     * RouteAddPostCommand - Fail #0 - DBException - orderManager.add
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteAddPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RouteAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("train_number")).thenReturn("A1");
        when(request.getParameter("station_departure")).thenReturn("2");
        when(session.getAttribute("station_arrival")).thenReturn("3");
        when(session.getAttribute("date_departure")).thenReturn("2222");
        when(session.getAttribute("date_arrival")).thenReturn("2221");
        when(session.getAttribute("travel_cost")).thenReturn("33");
        when(session.getAttribute("seats_reserved")).thenReturn("21");
        when(session.getAttribute("seats_total")).thenReturn("12");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(routeManager).add(any(Route.class));

        new RouteAddPostCommand().execute(request, response);
    }

    /**
     * RouteDeleteCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteDeleteCommandTest() throws Exception {

        Whitebox.setInternalState(RouteDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new RouteDeleteCommand().execute(request, response);
        Assertions.assertEquals("/routes", result);
    }

    /**
     * RouteDeleteCommand - Fail #0 - DBException - orderManager.routeManager
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteDeleteCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RouteDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(routeManager).delete(anyInt());

        new RouteDeleteCommand().execute(request, response);
    }

    /**
     * RouteEditGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteEditGetCommandTest() throws Exception {

        Whitebox.setInternalState(RouteEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new RouteEditGetCommand().execute(request, response);
        Assertions.assertEquals("/route.jsp", result);
    }

    /**
     * RouteEditGetCommand - Fail #0 - DBException - routeManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteEditGetCommandTestFail0Test() throws Exception {

        Whitebox.setInternalState(RouteEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(routeManager.get(1)).thenThrow(new DBException("DBException", null));

        new RouteEditGetCommand().execute(request, response);
    }

    /**
     * RouteAddGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteAddGetCommandTest() throws Exception {

        Whitebox.setInternalState(RouteAddGetCommand.class, "logger", mockLogger);

        String result = new RouteAddGetCommand().execute(request, response);
        Assertions.assertEquals("/route.jsp", result);
    }

    /**
     * RouteAddGetCommand - Fail #0 - DBException - stationManager.getAll
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteAddGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RouteAddGetCommand.class, "logger", mockLogger);

        when(stationManager.getAll()).thenThrow(new DBException("DBException", null));

        new RouteAddGetCommand().execute(request, response);
    }

    /**
     * RouteEditPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteEditPostCommandTest() throws Exception {

        Whitebox.setInternalState(RouteEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(request.getParameter("train_number")).thenReturn("A1");
        when(request.getParameter("station_departure")).thenReturn("2");
        when(session.getAttribute("station_arrival")).thenReturn("3");
        when(session.getAttribute("date_departure")).thenReturn("2222");
        when(session.getAttribute("date_arrival")).thenReturn("2221");
        when(session.getAttribute("travel_cost")).thenReturn("33");
        when(session.getAttribute("seats_reserved")).thenReturn("21");
        when(session.getAttribute("seats_total")).thenReturn("12");

        String result = new RouteEditPostCommand().execute(request, response);
        Assertions.assertEquals("/routes/edit/1", result);
    }

    /**
     * RouteEditPostCommand - Fail #0 - DBException - routeManager.update
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteEditPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RouteEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(request.getParameter("train_number")).thenReturn("A1");
        when(request.getParameter("station_departure")).thenReturn("2");
        when(session.getAttribute("station_arrival")).thenReturn("3");
        when(session.getAttribute("date_departure")).thenReturn("2222");
        when(session.getAttribute("date_arrival")).thenReturn("2221");
        when(session.getAttribute("travel_cost")).thenReturn("33");
        when(session.getAttribute("seats_reserved")).thenReturn("21");
        when(session.getAttribute("seats_total")).thenReturn("12");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(routeManager).update(anyInt(), any(Route.class));

        new RouteEditPostCommand().execute(request, response);
    }

    /**
     * RouteInfoCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void RouteInfoCommandTest() throws Exception {

        Whitebox.setInternalState(RouteInfoCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        Paginator paginator = PowerMockito.mock(Paginator.class);
        when(orderManager.getPaginated(any(QueryMaker.class))).thenReturn(paginator);
        when(paginator.getData()).thenReturn(new ArrayList<Order>());

        String result = new RouteInfoCommand().execute(request, response);
        Assertions.assertEquals("/routeInfo.jsp", result);
    }

    /**
     * RouteInfoCommand - Fail #0 - DBException - routeManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteInfoCommandFail0Test() throws Exception {

        Whitebox.setInternalState(RouteInfoCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(routeManager.get(anyInt())).thenThrow(new DBException("DBException", null));

        new RouteInfoCommand().execute(request, response);
    }

    /**
     * RouteInfoCommand - Fail #1 - DBException - routeManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void RouteInfoCommandFail1Test() throws Exception {

        Whitebox.setInternalState(RouteInfoCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        Paginator paginator = PowerMockito.mock(Paginator.class);
        when(orderManager.getPaginated(any(QueryMaker.class))).thenThrow(new DBException("DBException", null));

        new RouteInfoCommand().execute(request, response);
    }

}


