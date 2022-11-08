package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Order.OrdersListCommand;
import com.itermit.railway.command.Order.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Order;
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
 * Order Commands tests.
 * <p>
 * Includes test for:
 * <p>
 * OrdersListCommand, OrdersListCommandFail0, OrderAddGetCommand, OrderAddGetCommandFail0,
 * OrderAddPostCommand, OrderAddPostCommandFail0, OrderDeleteCommand,
 * OrderDeleteCommandFail0, OrderEditGetCommandFail0, OrderEditGetCommand,
 * OrderEditPostCommand, OrderEditPostCommandFail0.
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
public class OrderCommandTest {

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
     * OrdersListCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrdersListCommandTest() throws Exception {

        Whitebox.setInternalState(OrdersListCommand.class, "logger", mockLogger);

        String result = new OrdersListCommand().execute(request, response);
        Assertions.assertEquals("/orders.jsp", result);
    }

    /**
     * OrdersListCommand - Fail #0 - DBException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void OrdersListCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrdersListCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null)).when(orderManager).getAll();

        new OrdersListCommand().execute(request, response);
    }

    /**
     * OrderAddGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderAddGetCommandTest() throws Exception {

        Whitebox.setInternalState(OrderAddGetCommand.class, "logger", mockLogger);

        String result = new OrderAddGetCommand().execute(request, response);
        Assertions.assertEquals("/order.jsp", result);
    }

    /**
     * OrderAddGetCommand - Fail #0 - DBException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void OrderAddGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrdersListCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null)).when(userManager).getAll();

        new OrderAddGetCommand().execute(request, response);
    }

    /**
     *  OrderAddPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderAddPostCommandTest() throws Exception {

        Whitebox.setInternalState(OrderAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("user")).thenReturn("1");
        when(request.getParameter("route")).thenReturn("1");

        String result = new OrderAddPostCommand().execute(request, response);
        Assertions.assertEquals("/orders", result);
    }

    /**
     * AuthRegisterCommand - Fail #0 - DBException - orderManager.add
     *
     * @throws Exception
     */
    /* AuthRegisterCommand - Fail #0 - DBException - orderManager.add */
    @Test(expected = CommandException.class)
    public void OrderAddPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrderAddPostCommand.class, "logger", mockLogger);

        when(request.getParameter("user")).thenReturn("1");
        when(request.getParameter("route")).thenReturn("1");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).add(any(Order.class));

        new OrderAddPostCommand().execute(request, response);
    }

    /**
     * OrderDeleteCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderDeleteCommandTest() throws Exception {

        Whitebox.setInternalState(OrderDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new OrderDeleteCommand().execute(request, response);
        Assertions.assertEquals("/orders", result);
    }

    /**
     * OrderDeleteCommand - Fail #0 - DBException - orderManager.delete
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void OrderDeleteCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrderDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).delete(anyInt());

        String result = new OrderDeleteCommand().execute(request, response);
        Assertions.assertEquals("/orders", result);
    }

    /**
     * OrderEditGetCommand - Fail #0 - DBException - orderManager.delete
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void OrderEditGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrderEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);

        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);
        when(orderManager.get(1)).thenThrow(new DBException("DBException", null));

        String result = new OrderEditGetCommand().execute(request, response);
        Assertions.assertEquals("/order.jsp", result);
    }

    /**
     * OrderEditGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderEditGetCommandTest() throws Exception {

        Whitebox.setInternalState(OrderEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new OrderEditGetCommand().execute(request, response);
        Assertions.assertEquals("/order.jsp", result);
    }

    /**
     * OrderEditPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void OrderEditPostCommandTest() throws Exception {

        Whitebox.setInternalState(OrderEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(request.getParameter("user")).thenReturn("1");
        when(request.getParameter("route")).thenReturn("1");

        String result = new OrderEditPostCommand().execute(request, response);
        Assertions.assertEquals("/orders/edit/1", result);
    }

    /**
     * OrderEditPostCommand - Fail #0 - DBException - orderManager.update
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void OrderEditPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(OrderEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        when(request.getParameter("user")).thenReturn("1");
        when(request.getParameter("route")).thenReturn("1");

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(orderManager).update(anyInt(), any(Order.class));

        String result = new OrderEditPostCommand().execute(request, response);
        Assertions.assertEquals("/orders/edit/1", result);
    }


}


