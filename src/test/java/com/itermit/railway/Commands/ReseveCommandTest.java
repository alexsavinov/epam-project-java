package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Order.*;
import com.itermit.railway.command.Reserve.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Order;
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
import java.util.Properties;

import static org.mockito.Mockito.*;
/**
 * Auth Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * ReservesListCommand, ReservesListCommandFail0, ReservesListCommandFail1,
 * ReserveAddPostCommand, ReserveAddPostCommandFail0, ReserveDeleteCommand,
 * ReserveDeleteCommandFail0, ReserveDeleteCommandFail1, ReserveEditGetCommand,
 * ReserveEditGetCommandFail0, ReserveEditGetCommandFail1, ReservesGroupedListCommand,
 * ReservesGroupedListCommandTestFail0.
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
        UserManager.class,
        Transport.class
})
public class ReseveCommandTest {

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

        mockLogger = mock(Logger.class);
    }

    /**
     * ReservesListCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void ReservesListCommandTest() throws Exception {

        Whitebox.setInternalState(ReservesListCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new ReservesListCommand().execute(request, response);
        Assertions.assertEquals("/reservesGrouped.jsp", result);
    }

    /**
     * ReservesListCommand - Fail #0 - DBException routeManager
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReservesListCommandFail0Test() throws Exception {

        Whitebox.setInternalState(ReservesListCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null)).when(routeManager).get(anyInt());

        new ReservesListCommand().execute(request, response);
    }

    /**
     * ReservesListCommand - Fail #1 - DBException orderManager
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReservesListCommandFail1Test() throws Exception {

        Whitebox.setInternalState(ReservesListCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).getFiltered(any(QueryMaker.class));

        new ReservesListCommand().execute(request, response);
    }

    /**
     * ReserveAddPostCommand - Normal case
     *
     * @throws Exception
     */
    /* ReserveAddPostCommand - Normal case */
    @Test
    public void ReserveAddPostCommandTest() throws Exception {

        Whitebox.setInternalState(ReserveAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("route_id")).thenReturn("1");
        when(request.getParameter("seats")).thenReturn("2");
        when(session.getAttribute("userid")).thenReturn(3);

        String result = new ReserveAddPostCommand().execute(request, response);
        Assertions.assertEquals("/reserves/grouped", result);
    }

    /**
     * ReserveAddPostCommand - Fail #0 - DBException - orderManager.add
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReserveAddPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(ReserveAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("route_id")).thenReturn("1");
        when(request.getParameter("seats")).thenReturn("2");
        when(session.getAttribute("userid")).thenReturn(3);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).add(any(Order.class));

        String result = new ReserveAddPostCommand().execute(request, response);
        Assertions.assertEquals("/orders", result);
    }

    /**
     * ReserveDeleteCommand - Normal case
     *
     * @throws Exception
     */
    /* ReserveDeleteCommand - Normal case */
    @Test
    public void ReserveDeleteCommandTest() throws Exception {

        Whitebox.setInternalState(ReserveDeleteCommand.class, "logger", mockLogger);

        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("route_id")).thenReturn("11");
        when(request.getParameter("seats")).thenReturn("2");

        String result = new ReserveDeleteCommand().execute(request, response);
        Assertions.assertEquals("/reserve.jsp", result);
    }

    /**
     * ReserveDeleteCommand - Fail #0 - DBException - orderManager.deleteReserve
     *
     * @throws Exception
     */
    /* ReserveDeleteCommand - Fail #0 - DBException - orderManager.deleteReserve */
    @Test(expected = CommandException.class)
    public void ReserveDeleteCommandFail0Test() throws Exception {

        Whitebox.setInternalState(ReserveDeleteCommand.class, "logger", mockLogger);

        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("seats")).thenReturn("2");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).deleteReserve(anyInt(), anyInt());

        new ReserveDeleteCommand().execute(request, response);
    }

    /**
     * ReserveDeleteCommand - Fail #1 - DBException - orderManager.deleteReserve
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReserveDeleteCommandFail1Test() throws Exception {

        Whitebox.setInternalState(ReserveDeleteCommand.class, "logger", mockLogger);

        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("route_id")).thenReturn("11");
        when(request.getParameter("seats")).thenReturn("2");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).getFiltered(any(QueryMaker.class));

        new ReserveDeleteCommand().execute(request, response);
    }

    /**
     * ReserveEditPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void ReserveEditGetCommandTest() throws Exception {

        Whitebox.setInternalState(OrderEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new ReserveEditGetCommand().execute(request, response);
        Assertions.assertEquals("/reserve.jsp", result);
    }

    /**
     * ReserveEditPostCommand - Fail #0 - DBException - routeManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReserveEditGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(ReserveEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(routeManager.get(1)).thenThrow(new DBException("DBException", null));

        new ReserveEditGetCommand().execute(request, response);
    }

    /**
     * ReserveEditPostCommand - Fail #1 - CommandException - getIdFromRequest
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReserveEditGetCommandFail1Test() throws Exception {

        Whitebox.setInternalState(ReserveEditGetCommand.class, "logger", mockLogger);

        when(routeManager.get(1)).thenThrow(new DBException("DBException", null));

        String result = new ReserveEditGetCommand().execute(request, response);
        Assertions.assertEquals("/reserve.jsp", result);
    }

    /**
     * ReservesGroupedListCommandTest - Normal case
     *
     * @throws Exception
     */
    @Test
    public void ReservesGroupedListCommandTest() throws Exception {

        Whitebox.setInternalState(ReservesGroupedListCommand.class, "logger", mockLogger);

        when(session.getAttribute("userid")).thenReturn(1);

        String result = new ReservesGroupedListCommand().execute(request, response);
        Assertions.assertEquals("/reservesGrouped.jsp", result);
    }

    /**
     * ReservesGroupedListCommandTest - Fail #0 - DBException - orderManager.getGroupedByRouteOfUser
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void ReservesGroupedListCommandTestFail0Test() throws Exception {

        Whitebox.setInternalState(ReservesGroupedListCommand.class, "logger", mockLogger);

        when(session.getAttribute("userid")).thenReturn(1);

        when(orderManager.getGroupedByRouteOfUser(1))
                .thenThrow(new DBException("DBException", null));

        new ReservesGroupedListCommand().execute(request, response);
    }

}


