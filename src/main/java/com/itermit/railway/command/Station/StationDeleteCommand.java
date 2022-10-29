package com.itermit.railway.command.Station;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to delete Station.
 * <p>
 * Processes POST-Request.
 * Deletes Station from database.
 *
 * @author O.Savinov
 */
public class StationDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationDeleteCommand.class);

    /**
     * Command execution.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Address string
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        try {
            StationManager.getInstance().delete(id);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages", "Station deleted!");
        request.getSession().setAttribute("url", "/stations");
        request.setAttribute("action", "delete");

        return "/stations";
    }

}
