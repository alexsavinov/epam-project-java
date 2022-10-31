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
import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.Order.OrderAddGetCommand;
import com.itermit.railway.command.Order.OrderAddPostCommand;
import com.itermit.railway.command.Search.SearchResetCommand;
import com.itermit.railway.controller.*;
import com.itermit.railway.db.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ServletTest extends Mockito {


    @Test
    public void AuthServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final User user = mock(User.class);
        when(user.getId()).thenReturn(2);

        final AuthServlet authServlet = mock(AuthServlet.class);
//        String result = new AuthLoginCommand().execute(request, response);

//        String result = authServlet.doGet(request, response);

        doThrow(new RuntimeException()).when(authServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(authServlet).doPost(request, response);

//        assertNull(result);
    }

    @Test
    public void InitServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final User user = mock(User.class);
        when(user.getId()).thenReturn(2);

        final InitServlet initServlet = mock(InitServlet.class);
//        String result = new AuthLoginCommand().execute(request, response);

//        String result = authServlet.doGet(request, response);

        doThrow(new RuntimeException()).when(initServlet).doGet(request, response);
        doThrow(new RuntimeException()).when(initServlet).doPost(request, response);

//        assertNull(result);
    }

    @Test
    public void OrderServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final OrderServlet servlet = mock(OrderServlet.class);

        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
        doThrow(new RuntimeException()).when(servlet).doPost(request, response);
    }

    @Test
    public void ReserveServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final ReserveServlet servlet = mock(ReserveServlet.class);

        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
        doThrow(new RuntimeException()).when(servlet).doPost(request, response);
    }

    @Test
    public void RouteServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final RouteServlet servlet = mock(RouteServlet.class);

        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
        doThrow(new RuntimeException()).when(servlet).doPost(request, response);
    }

    @Test
    public void SearchServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final SearchServlet servlet = mock(SearchServlet.class);

        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
        doThrow(new RuntimeException()).when(servlet).doPost(request, response);
    }

    @Test
    public void StationServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final StationServlet servlet = mock(StationServlet.class);

        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
        doThrow(new RuntimeException()).when(servlet).doPost(request, response);
    }

    @Test
    public void UserServletTest() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final UserServlet servlet = mock(UserServlet.class);

//        doThrow(new RuntimeException()).when(servlet).doGet(request, response);
//        doThrow(new RuntimeException()).when(servlet).doPost(request, response);

        when(request.getRequestURI()).thenReturn("/users");
        servlet.doGet(request, response);

//        request.
//
//        servlet.doPost(request, response);

        System.out.println(request.getAttribute("error"));


    }


//    @Test
//    public void CommandHandlerTest() throws Exception {
//
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
//        CommandHandler commandHandler = mock(CommandHandler.class);
//        when(CommandHandler.processRedirect("commandName", request, response)).thenReturn(commandHandler.toString());
//
//        final CommandContainer commandContainer = mock(CommandContainer.class);
//        when(CommandContainer.runCommand(request, response, null)).thenReturn(null);
////        when(CommandContainer.getCommand("commandName")).thenReturn(null);
//
//
////        when(commandContainer). thenReturn(writer);
//        final CommandHandler servlet = mock(CommandHandler.class);
//
////        doThrow(new RuntimeException()).when(servlet).processForward("commandName", request, response);
////        doThrow(new RuntimeException()).when(servlet).processRedirect("commandName", request, response);
//    }

}

