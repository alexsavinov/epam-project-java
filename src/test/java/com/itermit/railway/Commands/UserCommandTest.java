package com.itermit.railway.Commands;


import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.command.User.*;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.User;
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
 * User Commands tests.
 * <p>
 * Includes tests for:
 * <p>
 * UsersListCommand, UsersListCommandFail0, UserAddGetCommand,
 * UserAddPostCommand, UserAddPostCommandFail0, UserDeleteCommand,
 * UserDeleteCommandFail0, UserEditGetCommand, UserEditGetCommandFail0,
 * UserEditPostCommand, UserEditPostCommandFail0.
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
        UserManager.class,
        Transport.class
})
public class UserCommandTest {

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

        mockLogger = mock(Logger.class);
    }

    /**
     * UsersListCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UsersListCommandTest() throws Exception {

        Whitebox.setInternalState(UsersListCommand.class, "logger", mockLogger);

        String result = new UsersListCommand().execute(request, response);

        Assertions.assertEquals("/users.jsp", result);
    }

    /**
     * UsersListCommand - Fail #0 - DBException
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void UsersListCommandFail0Test() throws Exception {

        Whitebox.setInternalState(UsersListCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null)).when(userManager).getAll();

        new UsersListCommand().execute(request, response);
    }

    /**
     * UserAddGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserAddGetCommandTest() throws Exception {

        Whitebox.setInternalState(UserAddGetCommand.class, "logger", mockLogger);

        String result = new UserAddGetCommand().execute(request, response);

        Assertions.assertEquals("/user.jsp", result);
    }

    /**
     * UserAddPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserAddPostCommandTest() throws Exception {

        Whitebox.setInternalState(UserAddPostCommand.class, "logger", mockLogger);

        String result = new UserAddPostCommand().execute(request, response);

        Assertions.assertEquals("/users", result);
    }

    /**
     * UserAddPostCommand - Fail #0 - DBException - stationManager.add
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void UserAddPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(UserAddPostCommand.class, "logger", mockLogger);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).add(any(User.class));

        String result = new UserAddPostCommand().execute(request, response);

        Assertions.assertEquals("/users", result);
    }

    /**
     * UserDeleteCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserDeleteCommandTest() throws Exception {

        Whitebox.setInternalState(UserDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new UserDeleteCommand().execute(request, response);
        Assertions.assertEquals("/users", result);
    }

    /**
     * UserDeleteCommand - Fail #0 - DBException - stationManager.delete
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void UserDeleteCommandFail0Test() throws Exception {

        Whitebox.setInternalState(UserDeleteCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).delete(anyInt());

        new UserDeleteCommand().execute(request, response);
    }

    /**
     * UserEditGetCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserEditGetCommandTest() throws Exception {

        Whitebox.setInternalState(UserEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new UserEditGetCommand().execute(request, response);

        Assertions.assertEquals("/user.jsp", result);
    }

    /**
     * UserEditGetCommand - Fail #0 - DBException - userManager.get
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void UserEditGetCommandFail0Test() throws Exception {

        Whitebox.setInternalState(UserEditGetCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).get(anyInt());

        new UserEditGetCommand().execute(request, response);
    }

    /**
     * UserEditPostCommand - Normal case
     *
     * @throws Exception
     */
    @Test
    public void UserEditPostCommandTest() throws Exception {

        Whitebox.setInternalState(UserEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        String result = new UserEditPostCommand().execute(request, response);

        Assertions.assertEquals("/users", result);
    }

    /**
     * UserEditPostCommand - Fail #0 - DBException - stationManager.update
     *
     * @throws Exception
     */
    @Test(expected = CommandException.class)
    public void UserEditPostCommandFail0Test() throws Exception {

        Whitebox.setInternalState(UserEditPostCommand.class, "logger", mockLogger);

        PowerMockito.mockStatic(CommandContainer.class);
        when(CommandContainer.getIdFromRequest(request)).thenReturn(1);

        PowerMockito.doThrow(new DBException("DBException", null))
                .when(userManager).update(anyInt(), any(User.class));

        String result = new UserEditPostCommand().execute(request, response);

        Assertions.assertEquals("/users", result);
    }

}
