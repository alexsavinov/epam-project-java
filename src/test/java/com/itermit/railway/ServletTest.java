package com.itermit.railway;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.controller.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.PropertiesLoader;
import com.itermit.railway.utils.QueryMaker;
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
import org.powermock.reflect.Whitebox;

import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.RequestDispatcher;
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
 * Servlets - Tests.
 * <p>
 * Includes test for:
 * <p>
 * InitServlet, AuthServlet, SearchServlet,
 * <p>
 * OrderServlet, RouteServlet, StationServlet, ReserveServlet.
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
        Transport.class
})
public class ServletTest {

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
     * AuthServlet Test
     *
     * @throws Exception
     */
    @Test
    public void AuthServletTest() throws Exception {

        Whitebox.setInternalState(AuthServlet.class, "logger", mockLogger);

        AuthServlet authServlet = PowerMockito.mock(AuthServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(authServlet).doGet(request, response);

        when(request.getRequestURI()).thenReturn("/profile");
        new AuthServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/login");
        new AuthServlet().doGet(request, response);
        new AuthServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/register");
        new AuthServlet().doGet(request, response);
        new AuthServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/logout");
        new AuthServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/activate");
        new AuthServlet().doGet(request, response);
        new AuthServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new AuthServlet().doGet(request, response);
        new AuthServlet().doPost(request, response);

    }

    /**
     * InitServlet Test
     *
     * @throws Exception
     */
    @Test
    public void InitServletTest() throws Exception {

        Whitebox.setInternalState(InitServlet.class, "logger", mockLogger);

        InitServlet initServlet = PowerMockito.mock(InitServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);

        when(request.getRequestURI()).thenReturn("/error");
        when(request.getAttribute("error")).thenReturn("error");

        new InitServlet().doGet(request, response);
        new InitServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new InitServlet().doGet(request, response);
        new InitServlet().doPost(request, response);

    }

    /**
     * InitServlet Fail #0 Test
     *
     * @throws Exception
     */
    @Test
    public void InitServletFail0Test() throws Exception {

        Whitebox.setInternalState(InitServlet.class, "logger", mockLogger);

        InitServlet initServlet = PowerMockito.mock(InitServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);

        when(request.getRequestURI()).thenReturn("/error");
        when(response.getWriter()).thenThrow(new IOException());

        new InitServlet().doGet(request, response);

    }

    /**
     * OrderServlet Test
     *
     * @throws Exception
     */
    @Test
    public void OrderServletTest() throws Exception {

        Whitebox.setInternalState(OrderServlet.class, "logger", mockLogger);

        OrderServlet initServlet = PowerMockito.mock(OrderServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);

        when(request.getRequestURI()).thenReturn("/orders");
        new OrderServlet().doGet(request, response);
        new OrderServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/orders/edit");
        new OrderServlet().doGet(request, response);
        new OrderServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/orders/delete");
        new OrderServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/orders/add");
        new OrderServlet().doGet(request, response);

        when(request.getParameter("user")).thenReturn("1");
        when(request.getParameter("route")).thenReturn("2");
        new OrderServlet().doPost(request, response);


        when(request.getRequestURI()).thenReturn("//");
        new OrderServlet().doGet(request, response);
        new OrderServlet().doPost(request, response);

    }

    /**
     * ReserveServlet Test
     *
     * @throws Exception
     */
    @Test
    public void ReserveServletTest() throws Exception {

        Whitebox.setInternalState(ReserveServlet.class, "logger", mockLogger);

        ReserveServlet initServlet = PowerMockito.mock(ReserveServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("/reserves");
        new ReserveServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/reserves/grouped");
        when(session.getAttribute("userid")).thenReturn("1");
        new ReserveServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/reserves/delete");
        when(request.getParameter("order_id")).thenReturn("11");
        when(request.getParameter("route_id")).thenReturn("31");
        when(request.getParameter("seats")).thenReturn("22");
        new ReserveServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/reserves/edit");
        new ReserveServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/reserves/add");
        new ReserveServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new ReserveServlet().doGet(request, response);
        new ReserveServlet().doPost(request, response);

    }

    /**
     * RouteServlet Test
     *
     * @throws Exception
     */
    @Test
    public void RouteServletTest() throws Exception {

        Whitebox.setInternalState(RouteServlet.class, "logger", mockLogger);

        RouteServlet initServlet = PowerMockito.mock(RouteServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("/routes");
        new RouteServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/routes/info");
        new RouteServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/routes/grouped");
        when(session.getAttribute("userid")).thenReturn("1");
        new RouteServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/routes/delete");
        when(request.getParameter("order_id")).thenReturn("11");
        when(request.getParameter("route_id")).thenReturn("31");
        when(request.getParameter("seats")).thenReturn("22");
        new RouteServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/routes/edit");
        new RouteServlet().doGet(request, response);
        new RouteServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/routes/add");
        new RouteServlet().doGet(request, response);
        new RouteServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new RouteServlet().doGet(request, response);
        new RouteServlet().doPost(request, response);
    }

    /**
     * SearchServlet Test
     *
     * @throws Exception
     */
    @Test
    public void SearchServletTest() throws Exception {

        Whitebox.setInternalState(SearchServlet.class, "logger", mockLogger);

        SearchServlet initServlet = PowerMockito.mock(SearchServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new SearchServlet().doGet(request, response);
        new SearchServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/search");
        new SearchServlet().doGet(request, response);

        Paginator paginator = PowerMockito.mock(Paginator.class);
        when(routeManager.getPaginated(any(QueryMaker.class))).thenReturn(paginator);
        when(paginator.getData()).thenReturn(new ArrayList<Route>());
        new SearchServlet().doPost(request, response);

        when(request.getParameter("action")).thenReturn("reset");
        new SearchServlet().doPost(request, response);
    }

    /**
     * StationServlet Test
     *
     * @throws Exception
     */
    @Test
    public void StationServletTest() throws Exception {

        Whitebox.setInternalState(StationServlet.class, "logger", mockLogger);

        StationServlet initServlet = PowerMockito.mock(StationServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new StationServlet().doGet(request, response);
        new StationServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/stations");
        new StationServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/stations/edit");
        new StationServlet().doGet(request, response);
        new StationServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/stations/delete");
        new StationServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/stations/add");
        new StationServlet().doGet(request, response);
        new StationServlet().doPost(request, response);
    }

    /**
     * UserServlet Test
     *
     * @throws Exception
     */
    @Test
    public void UserServletTest() throws Exception {

        Whitebox.setInternalState(UserServlet.class, "logger", mockLogger);

        UserServlet initServlet = PowerMockito.mock(UserServlet.class);

        RequestDispatcher requestDispatcher = PowerMockito.mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("//");
        new UserServlet().doGet(request, response);
        new UserServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/users");
        new UserServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/users/edit");
        new UserServlet().doGet(request, response);
        new UserServlet().doPost(request, response);

        when(request.getRequestURI()).thenReturn("/users/delete");
        new UserServlet().doGet(request, response);

        when(request.getRequestURI()).thenReturn("/users/add");
        new UserServlet().doGet(request, response);
        new UserServlet().doPost(request, response);
    }

}


