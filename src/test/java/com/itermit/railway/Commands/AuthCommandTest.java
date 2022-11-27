package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthActivateCommand;
import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.Auth.AuthLogoutCommand;
import com.itermit.railway.command.Auth.AuthRegisterCommand;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.PropertiesLoader;
import com.itermit.railway.utils.SendEmailUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.apache.logging.log4j.Logger;
import org.powermock.reflect.Whitebox;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Auth Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * AuthActivateCommand, AuthLogoutCommand, AuthLoginCommand, AuthLoginCommandFail1,
 * AuthLoginCommandFail2, AuthRegisterCommand, AuthRegisterCommandFail0, AuthRegisterCommandFail1,
 * AuthRegisterCommandFail2, AuthRegisterCommandFail3.
 *
 *
 *
 *
 *
 *
 * @author O.Savinov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        AuthLoginCommand.class,
        DBManager.class,
        InternetAddress.class,
        LoggerFactory.class,
        PropertiesLoader.class,
        Transport.class,
        SendEmailUtil.class,
        Session.class,
        User.class,
        UserManager.class})
public class AuthCommandTest {

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
    UserManager userManager;
    @Mock
    Logger mockLogger;

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

        PowerMockito.mockStatic(UserManager.class);
        userManager = PowerMockito.mock(UserManager.class);
        when(UserManager.getInstance()).thenReturn(userManager);

        mockLogger = mock(Logger.class);
    }

    /**
     * AuthActivateCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void AuthActivateCommandTest() throws Exception {
        String result = new AuthActivateCommand().execute(request, response);
        Assertions.assertEquals("/login", result);
    }

    /**
     * AuthLogoutCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void AuthLogoutCommandTest() throws Exception {
        String result = new AuthLogoutCommand().execute(request, response);
        Assertions.assertEquals("/", result);
    }

    /**
     * AuthLoginCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void AuthLoginCommandTest() throws Exception {

        Whitebox.setInternalState(AuthLoginCommand.class, "logger", mockLogger);

        User user = new User.Builder()
                .withId(1)
                .withName("name")
                .withPassword(User.passwordEncrypt("password"))
                .withIsActive(true)
                .build();


        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("password")).thenReturn("password");

        when(userManager.get(any(User.class))).thenReturn(user);

        String result = new AuthLoginCommand().execute(request, response);
        Assertions.assertEquals("/profile", result);
    }

    /**
     * AuthLoginCommand - Fail #1
     *
     * @throws Exception
     */
    @Test
    public void AuthLoginCommandFail1Test() throws Exception {

        Whitebox.setInternalState(AuthLoginCommand.class, "logger", mockLogger);

        String result = new AuthLoginCommand().execute(request, response);
        Assertions.assertEquals("/login", result);
    }

    /**
     * AuthLoginCommand - Normal case
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void AuthLoginCommandFail2Test() throws Exception {

        Whitebox.setInternalState(AuthLoginCommand.class, "logger", mockLogger);

        given(userManager.get(any(User.class))).willAnswer(invocation -> {
            throw new DBException("abc msg", null);
        });
        new AuthLoginCommand().execute(request, response);
    }

    /**
     * AuthRegisterCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void AuthRegisterCommandTest() throws Exception {

        when(request.getParameter("name")).thenReturn("name111");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("email@");

        when(UserManager.getInstance()).thenReturn(userManager);
        when(userManager.get(any(User.class))).thenReturn(
                new User.Builder().withId(0).build() // id=1 - for error case
        );

        InternetAddress address = new InternetAddress("recipientEmail");
        PowerMockito.whenNew(InternetAddress.class)
                .withArguments(any(String.class))
                .thenReturn(address);

        String result = new AuthRegisterCommand().execute(request, response);
        Assertions.assertEquals("/login", result);

        when(userManager.get(any(User.class))).thenReturn(new User.Builder().withId(1).build());
        String result1 = new AuthRegisterCommand().execute(request, response);
        Assertions.assertEquals("/register", result1);

        when(userManager.get(any(User.class))).thenReturn(new User.Builder().withId(1).withIsActive(true).build());
        String result2 = new AuthRegisterCommand().execute(request, response);
        Assertions.assertEquals("/register", result2);

        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password1");
        when(request.getParameter("email")).thenReturn("email");
        when(userManager.get(any(User.class))).thenReturn(new User.Builder().withId(1).withIsActive(true).build());
        String result3 = new AuthRegisterCommand().execute(request, response);
        Assertions.assertEquals("/register", result3);

        when(userManager.get(any(User.class))).thenReturn(null);
        String result4 = new AuthRegisterCommand().execute(request, response);
        Assertions.assertEquals("/register", result4);
    }

    /**
     * AuthRegisterCommand - Fail #0 - MessagingException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void AuthRegisterCommandFail0Test() throws Exception {

        when(request.getParameter("name")).thenReturn("name111");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("email@");

        when(UserManager.getInstance()).thenReturn(userManager);
        when(userManager.get(any(User.class))).thenReturn(
                new User.Builder().withId(0).build());

        InternetAddress address = new InternetAddress("recipientEmail");

        PowerMockito.mockStatic(SendEmailUtil.class);

        /* Void method mock */
        PowerMockito.doThrow(new MessagingException()).when(SendEmailUtil.class, "sendEmail", anyString(), anyString(), anyString());

        PowerMockito.whenNew(InternetAddress.class)
                .withArguments(any(String.class))
                .thenReturn(address);

        new AuthRegisterCommand().execute(request, response);
    }

    /**
     * AuthRegisterCommand - Fail #1 - UnsupportedEncodingException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void AuthRegisterCommandFail1Test() throws Exception {

        when(UserManager.getInstance()).thenReturn(userManager);

        when(userManager.get(any(User.class))).thenReturn(
                new User.Builder().withId(0).build()
        );

        PowerMockito.mockStatic(URLEncoder.class);

        PowerMockito.doThrow(new UnsupportedEncodingException())
                .when(request).setCharacterEncoding("utf-8");

        new AuthRegisterCommand().execute(request, response);
    }

    /**
     * AuthRegisterCommand - Fail #2 - DBException - userManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void AuthRegisterCommandFail2Test() throws Exception {

        when(request.getParameter("name")).thenReturn("name111");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("email@");

        when(UserManager.getInstance()).thenReturn(userManager);

        when(userManager.get(any(User.class))).thenReturn(
                new User.Builder().withId(0).build()
        );

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).get(any(User.class));

        new AuthRegisterCommand().execute(request, response);
    }

    /**
     * AuthRegisterCommand - Fail #3 - DBException - UserManager add
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void AuthRegisterCommandFail3Test() throws Exception {

        when(request.getParameter("name")).thenReturn("name111");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("email@");

        when(UserManager.getInstance()).thenReturn(userManager);

        when(userManager.get(any(User.class))).thenReturn(
                new User.Builder().withId(0).build());

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).add(any(User.class));

        new AuthRegisterCommand().execute(request, response);
    }

}
