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

import com.itermit.railway.command.Search.SearchResetCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.mockito.Mock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
//import org.mockito.MockitoAnnotations;

//import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class testTtt extends Mockito {


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
    public void testServlet() throws Exception {

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
        System.out.println("logPath " + System.getProperty("logPath"));
        System.out.println("PropertyName1 " + System.getProperty("PropertyName1"));
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

        String result = new SearchResetCommand().execute(request, response);

        System.out.println("daterange: " + session.getAttribute("daterange"));


//        when(request.getSession().getAttribute("role")).thenReturn("admin");
//        verify(request, atLeast(1)).getParameter("username"); // only if you want to verify username was called...
//        verify(request, atLeast(1)).getSession().getAttribute("station_departure_id"); // only if you want to verify username was called...
//        writer.flush(); // it may not have been flushed yet...
//        assertTrue(stringWriter.toString().contains("0"));
        assertTrue(result == isNull());
    }
}