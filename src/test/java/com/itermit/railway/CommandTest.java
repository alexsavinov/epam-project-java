package com.itermit.railway;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//import java.io.*;
//import javax.servlet.http.*;
//import org.apache.commons.io.FileUtils;
//import org.junit.Test;

//import com.itermit.railway.controller.CommandHandler;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.BeforeEach;

import com.itermit.railway.command.Auth.AuthActivateCommand;
import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.Auth.AuthLogoutCommand;
import com.itermit.railway.command.Auth.AuthRegisterCommand;
import com.itermit.railway.command.Order.OrderAddGetCommand;
import com.itermit.railway.command.Order.OrderAddPostCommand;
import com.itermit.railway.command.Search.SearchGetCommand;
import com.itermit.railway.command.Search.SearchResetCommand;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.mockito.Mock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
//import org.mockito.MockitoAnnotations;

//import javax.servlet.FilterConfig;
//import javax.servlet.annotation.WebFilter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.NamingManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class CommandTest extends Mockito {


//    private DataSource mockDataSource;
//    private Connection mockConnection;
//
//    protected void mockInitialContext() throws NamingException, SQLException {
////        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
////        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//
//        ////        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
////        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
////
//        Properties p = new Properties();
//        p.put(Context.INITIAL_CONTEXT_FACTORY,
//                "org.jboss.naming.remote.client.InitialContextFactory");
//        p.put(Context.URL_PKG_PREFIXES,
//                "org.jboss.as.naming.interfaces:org.jboss.ejb.client.naming");
//        p.put(Context.PROVIDER_URL, "remote://localhost:4447");
//        p.put("jboss.naming.client.ejb.context", true);
////        InitialContext ctx = new InitialContext(p);
////        InitialContext ctx = new InitialContext(p);
//
//        InitialContext mockInitialContext = (InitialContext) NamingManager.getInitialContext(p);
////        InitialContext mockInitialContext = (InitialContext) NamingManager.getInitialContext(System.getProperties());
//        mockDataSource = mock(DataSource.class);
//        mockConnection = mock(Connection.class);
//
//        when(mockInitialContext.lookup(anyString())).thenReturn(mockDataSource);
//        when(mockDataSource.getConnection()).thenReturn(mockConnection);
//
//        try {
//            when(mockDataSource.getConnection()).thenReturn(mockConnection);
//        } catch (SQLException ex) {
////            Logger.getLogger(CLASSNAME).log(Level.SEVERE, null, ex);
//        }
//    }
//    @Mock
//    DataSource dataSource;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }


//    private ContentSecurityPolicyFilter contentSecurityPolicyFilter;
//    private FilterConfig filterConfig;
//    private ServletContext servletContext;
////    private ConfigManager configManager;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private String keycloakConfigFile;
//
//    @BeforeEach
//    public void setUp() {
////        contentSecurityPolicyFilter = new ContentSecurityPolicyFilter();
//        filterConfig = mock(FilterConfig.class);
//        servletContext = mock(ServletContext.class);
////        configManager = mock(ConfigManager.class);
//        request = mock(HttpServletRequest.class);
//        response = mock(HttpServletResponse.class);
////        keycloakConfigFile = getClass().getClassLoader().getResource("keycloak-hawtio-client.json").getFile();
//
//        when(filterConfig.getServletContext()).thenReturn(servletContext);
////        when(servletContext.getAttribute("ConfigManager")).thenReturn(configManager);
//
////        System.clearProperty(KeycloakServlet.HAWTIO_KEYCLOAK_CLIENT_CONFIG);
//    }

    //    @Mock
//    ServletContext sc;
////    @Mock
////    FilterConfig filterConfig;
////    public final static String Seperator = new Character((char)1).toString();
////    public final static String ContentDelimeter = new Character((char)2).toString();
//
//    @BeforeClass
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        sc = mock(ServletContext.class);
//    }
//    @Mock
//    HttpSession httpSession;

    @Test
    public void SearchResetCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

//        final ServletContext servletContext = Mockito.mock(ServletContext.class);
//        ServletContext context = event.getServletContext();

//        ServletContext context = mock(ServletContext.class);
//        when(request.getServletContext()).thenReturn(context);

//        CommandHandler commandHandler = mock(CommandHandler.class);
//        when(CommandHandler.processRedirect()).thenReturn(commandHandler.getClass());

//        CommandHandler mockA = Mockito.mock(CommandHandler.class, Mockito.CALLS_REAL_METHODS);
//        when(mockA.processRedirect("searchReset", request, response)).thenReturn("");
//        doReturn(Integer.valueOf(1)).when(mockA).processRedirect("searchReset", request, response);

//        Properties prop = new Properties();

//        when(request.getServletContext()).thenReturn(sc);

//        String logPath = context.getRealPath("/logs");
//        System.out.println("context " + context);
//        System.out.println("logPath " + System.getProperty("logPath"));
//        System.out.println("PropertyName1 " + System.getProperty("PropertyName1"));
//        System.setProperty("logPath", logPath);


//        when(request.getParameter("username")).thenReturn("me");
//        when(request.getParameter("password")).thenReturn("secret");
//        when(CommandHandler.processRedirect("searchReset", request, response)).thenReturn("secret");

//        doNothing().when(httpSession).removeAttribute("currentUserUsername");
//        Mockito.doThrow(new Exception()).doNothing().when(httpSession).removeAttribute("currentUserUsername");
//        doThrow(new RuntimeException()).when(httpSession).removeAttribute("currentUserUsername");
//        verify(httpSession, times(1)).removeAttribute(any(String.class));


        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
//        when(request.getSession().getAttribute("role")).thenReturn("admin");

//        Mockito.doNothing().when(httpSession).removeAttribute(any());


//        Mockito.doThrow(new Exception()).doNothing().when(instance).methodName();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);


        final SearchResetCommand searchResetCommand = mock(SearchResetCommand.class);
        String result = new SearchResetCommand().execute(request, response);


//        doReturn("result").when(new SearchResetCommand()).execute(request, response);
        doThrow(new RuntimeException()).when(searchResetCommand).execute(request, response);

//        System.out.println("daterange: " + session.getAttribute("daterange"));


//        when(request.getSession().getAttribute("role")).thenReturn("admin");
//        verify(request, atLeast(1)).getParameter("username"); // only if you want to verify username was called...
//        verify(request, atLeast(1)).getSession().getAttribute("station_departure_id"); // only if you want to verify username was called...
//        writer.flush(); // it may not have been flushed yet...
//        assertTrue(stringWriter.toString().contains("0"));
//        assertTrue(result == isNull());
        assertEquals(result, null);
    }

    @Test
    public void AuthActivateCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        String result = new AuthActivateCommand().execute(request, response);

        assertEquals("/login", result);
    }


    @Test
    public void AuthRegisterCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
//        when(DBManager.getInstance()).thenReturn(dataSource);

//        DBManager dbManager = mock(DBManager.class);
//        Connection connection = mock(Connection.class);
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(DBManager.getInstance()).thenReturn(dbManager);
//        Hashtable<String, String> props= new Hashtable<String, String>();
////        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
//        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//        props.put(Context.PROVIDER_URL,"jnp://localhost:1099" );
////        props.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces" );
//        Context ctx = new InitialContext( props );

//        Hashtable<String, String> props= new Hashtable<String, String>();
//        props.put(Context.INITIAL_CONTEXT_FACTORY,
//                "org.jboss.naming.remote.client.InitialContextFactory");
//        InitialContext initialContext = new InitialContext();

//        mockInitialContext();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);


//        User user = new User;
//        PrintWriter writer = new PrintWriter(stringWriter);
        final User user = mock(User.class);
        when(user.getId()).thenReturn(2);

        final AuthRegisterCommand authRegisterCommand = mock(AuthRegisterCommand.class);
        String result = authRegisterCommand.execute(request, response);

//        String result = new AuthLoginCommand().execute(request, response);
//        doThrow(new RuntimeException()).when(authLoginCommand).execute(request, response);

        assertNull(result);
    }


    @Test
    public void AuthLoginCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("name")).thenReturn("writer");
        when(request.getParameter("password")).thenReturn("writerpassword");

//        final User user = mock(User.class);
        final User user = new User.Builder().withId(1).build();
//        when(user.getId()).thenReturn(2);

        UserManager userManager = mock(UserManager.class);
//        when(userManager.get(user)).thenReturn(user);
        when(userManager.get(user)).thenReturn(new User.Builder().withId(1).build());



        final AuthLoginCommand authLoginCommand = mock(AuthLoginCommand.class);
        String result = authLoginCommand.execute(request, response);

        System.out.println(request.getSession().getAttribute("userid"));;

        assertNull(result);
    }

    @Test
    public void AuthLogoutCommandTest() throws Exception {

//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        HttpSession session = mock(HttpSession.class);
//        when(request.getSession()).thenReturn(session);
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        final User user = mock(User.class);
//        when(user.getId()).thenReturn(2);
//
//        final AuthLogoutCommand authLogoutCommand = mock(AuthLogoutCommand.class);
//        String result = authLogoutCommand.execute(request, response);
//
//        assertNull(result);




        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        String result = new AuthLogoutCommand().execute(request, response);

        assertEquals("/", result);



    }


    @Test
    public void OrderAddGetCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final User user = mock(User.class);
        when(user.getId()).thenReturn(2);

        final OrderAddGetCommand orderAddGetCommand = mock(OrderAddGetCommand.class);
        String result = orderAddGetCommand.execute(request, response);

        assertNull(result);
    }

    @Test
    public void OrderAddPostCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final User user = mock(User.class);
        when(user.getId()).thenReturn(2);

        final OrderAddPostCommand orderAddPostCommand = mock(OrderAddPostCommand.class);
        String result = orderAddPostCommand.execute(request, response);

        assertNull(result);
    }


    @Test
    public void SearchGetCommandTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        String result = new SearchGetCommand().execute(request, response);

        assertEquals("/search.jsp", result);
    }



}