package com.itermit.railway.command.Station;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to add Station.
 * <p>
 * Processes POST-Request.
 * Adds new Station to database.
 *
 * @author O.Savinov
 */
public class StationAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationAddPostCommand.class);

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

        String name = request.getParameter("name");

        Station station = new Station.Builder().withName(name).build();
        try {
            StationManager.getInstance().add(station);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages", "Station " + name + " added!");

        return "/stations";
    }

}
