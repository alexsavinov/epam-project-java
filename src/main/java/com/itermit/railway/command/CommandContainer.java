package com.itermit.railway.command;

import com.itermit.railway.command.Auth.AuthActivateCommand;
import com.itermit.railway.command.Auth.AuthLoginCommand;
import com.itermit.railway.command.Auth.AuthLogoutCommand;
import com.itermit.railway.command.Auth.AuthRegisterCommand;
import com.itermit.railway.command.Order.*;
import com.itermit.railway.command.Reserve.*;
import com.itermit.railway.command.Route.*;
import com.itermit.railway.command.Search.SearchGetCommand;
import com.itermit.railway.command.Search.SearchPostCommand;
import com.itermit.railway.command.Search.SearchResetCommand;
import com.itermit.railway.command.Station.*;
import com.itermit.railway.command.User.*;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private static final Map<String, Command> commands;
    private static final Logger logger = LogManager.getLogger(CommandContainer.class);

    static {
        commands = new HashMap<>();
        /* AuthServlet */
        commands.put("authLogin", new AuthLoginCommand());
        commands.put("authLogout", new AuthLogoutCommand());
        commands.put("authRegister", new AuthRegisterCommand());
        commands.put("authActivate", new AuthActivateCommand());
        /* SearchServlet */
        commands.put("searchGet", new SearchGetCommand());
        commands.put("searchPost", new SearchPostCommand());
        commands.put("searchReset", new SearchResetCommand());
        /* UserServlet */
        commands.put("usersList", new UsersListCommand());
        commands.put("userAddGet", new UserAddGetCommand());
        commands.put("userAddPost", new UserAddPostCommand());
        commands.put("userEditGet", new UserEditGetCommand());
        commands.put("userEditPost", new UserEditPostCommand());
        commands.put("userDelete", new UserDeleteCommand());
        /* StationServlet */
        commands.put("stationsList", new StationsListCommand());
        commands.put("stationEditGet", new StationEditGetCommand());
        commands.put("stationEditPost", new StationEditPostCommand());
        commands.put("stationAddGet", new StationAddGetCommand());
        commands.put("stationAddPost", new StationAddPostCommand());
        commands.put("stationDelete", new StationDeleteCommand());
        /* RouteServlet */
        commands.put("routesList", new RoutesListCommand());
        commands.put("routeEditGet", new RouteEditGetCommand());
        commands.put("routeEditPost", new RouteEditPostCommand());
        commands.put("routeAddGet", new RouteAddGetCommand());
        commands.put("routeAddPost", new RouteAddPostCommand());
        commands.put("routeDelete", new RouteDeleteCommand());
        commands.put("routeInfo", new RouteInfoCommand());
        /* OrderServlet */
        commands.put("ordersList", new OrdersListCommand());
        commands.put("orderEditGet", new OrderEditGetCommand());
        commands.put("orderEditPost", new OrderEditPostCommand());
        commands.put("orderAddGet", new OrderAddGetCommand());
        commands.put("orderAddPost", new OrderAddPostCommand());
        commands.put("orderDelete", new OrderDeleteCommand());
        /* ReserveServlet */
        commands.put("reservesList", new ReservesListCommand());
        commands.put("reservesGroupedList", new ReservesGroupedListCommand());
        commands.put("reserveEditGet", new ReserveEditGetCommand());
//        commands.put("reserveEditPost", new ReserveEditPostCommand());
//        commands.put("reserveAddGet", new ReserveAddGetCommand());
        commands.put("reserveAddPost", new ReserveAddPostCommand());
        commands.put("reserveDelete", new ReserveDeleteCommand());
    }

    private CommandContainer() {
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public static void runCommand(HttpServletRequest request, HttpServletResponse response, String commandName) {

        logger.debug("#runCommand(request, response, commandName):  {}", commandName);

        if (commandName != null) {
            Command command = CommandContainer.getCommand(commandName);
            try {
                command.execute(request, response);
            } catch (DBException e) {
                logger.error("DBException. Command execution error! {}", e.getMessage());
                request.setAttribute("error", e.getMessage());
                try {
                    request.getRequestDispatcher("/error").forward(request, response);
                } catch (ServletException ex) {
                    logger.error("ServletException. Command execution error! {}", e.getMessage());
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    logger.error("RuntimeException. Command execution error! {}", e.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static int getIdFromRequest(HttpServletRequest request) throws DBException {

        int id = 0;
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            id = Integer.parseInt(request.getPathInfo().substring(1));
        }

        if (id == 0) {
            throw new DBException("Cannot get 'id' from request!", null);
        }
        return id;
    }

    public static String getTokenFromRequest(HttpServletRequest request) throws DBException {

        String token = "";
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            token = request.getPathInfo().substring(1);
        }

        if (token == null) {
            throw new DBException("Cannot get 'token' from request!", null);
        }
        return token;
    }

    public static String getErrorMessage(String defaultMessage, SQLException e) {
        if (e.getErrorCode() == 1451) {
            String nameDatabase = e.getMessage().split("REFERENCES")[1].split("`")[1];
            return "First you have to remove an element from the tables: " + nameDatabase;
        }
        return defaultMessage;
    }

    public static int getIntegerFromRequest(HttpServletRequest request, String param) {
        String paramValue = request.getParameter(param);
        if (paramValue != null) {
            return Integer.valueOf(paramValue);
        }
        return 0;
    }

    public static String getStringFromRequest(HttpServletRequest request, String param) {
        String paramValue = request.getParameter(param);
        if (paramValue != null) {
            return paramValue;
        }
        return "";
    }
}
