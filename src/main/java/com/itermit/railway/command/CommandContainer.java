package com.itermit.railway.command;

import com.itermit.railway.command.Auth.*;
import com.itermit.railway.command.Order.*;
import com.itermit.railway.command.Reserve.*;
import com.itermit.railway.command.Route.*;
import com.itermit.railway.command.Search.*;
import com.itermit.railway.command.Station.*;
import com.itermit.railway.command.User.*;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Command container.
 * <p>
 * Resolve command by name and runs related execute method.
 *
 * @author O.Savinov
 */
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
        commands.put("reserveAddPost", new ReserveAddPostCommand());
        commands.put("reserveDelete", new ReserveDeleteCommand());
    }

    /**
     * Default constructor
     */
    private CommandContainer() {
    }

    /**
     * Returns Command by its name.
     *
     * @param commandName String with command name
     * @return Command
     */
    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    /**
     * Runs Command with given name.
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param commandName String with command name
     * @return String with address to process
     */
    public static String runCommand(HttpServletRequest request, HttpServletResponse response, String commandName)
            throws CommandException {

        logger.debug("#runCommand(request, response, commandName):  {}", commandName);

        if (commandName != null) {
            Command command = CommandContainer.getCommand(commandName);
            try {
                return command.execute(request, response);
            } catch (DBException e) {
                logger.error("DBException. Command execution error! {}", e.getMessage());
                request.setAttribute("error", e.getMessage());
                throw new CommandException(e.getMessage(), e);
            }
        }

        return null;
    }

    /**
     * Parses id from URL.
     *
     * @param request HttpServletRequest
     * @return Integer value of certain id from HttpServletRequest
     */
    public static int getIdFromRequest(HttpServletRequest request)
            throws CommandException {

        int id = 0;
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            id = Integer.parseInt(request.getPathInfo().substring(1));
        }

        if (id == 0) {
            throw new CommandException("Cannot get 'id' from request!", null);
        }

        return id;
    }

    /**
     * Parses token from URL.
     *
     * @param request HttpServletRequest
     * @return String value of token from HttpServletRequest
     */
    public static String getTokenFromRequest(HttpServletRequest request)
            throws CommandException {

        String token = "";
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            token = request.getPathInfo().substring(1);
        }

        if (token == null) {
            throw new CommandException("Cannot get 'token' from request!", null);
        }

        return token;
    }

    /**
     * Parses table name from Error string.
     *
     * @param defaultMessage String with default error message
     * @return String with update error message
     */
    public static String getErrorMessage(String defaultMessage, SQLException e) {

        if (e.getErrorCode() == 1451) {
            String nameDatabase = e.getMessage().split("REFERENCES")[1].split("`")[1];
            return "First you must delete item from the tables: " + nameDatabase;
        }

        if (e.getErrorCode() == 1217) {
            /* "Cannot delete or update a parent row: a foreign key constraint fails" */
            return "Delete items like this: Orders >> Routes >> Other tables!";
        }

        return defaultMessage;
    }

    /**
     * Parses integer value from request.
     *
     * @param request HttpServletRequest
     * @return Integer value of given parameter name
     */
    public static int getIntegerFromRequest(HttpServletRequest request, String param) {

        String paramValue = request.getParameter(param);
        if (paramValue != null) {
            return Integer.valueOf(paramValue);
        }

        return 0;
    }

    /**
     * Parses string value from request.
     *
     * @param request HttpServletRequest
     * @return String value of given parameter name
     */
    public static String getStringFromRequest(HttpServletRequest request, String param) {

        String paramValue = request.getParameter(param);
        if (paramValue != null) {
            return paramValue;
        }

        return "";
    }
}
