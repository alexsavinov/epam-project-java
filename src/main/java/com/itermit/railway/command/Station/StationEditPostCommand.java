package com.itermit.railway.command.Station;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to edit Station.
 * <p>
 * Processes GET-Request.
 * Displays Form to submit.
 *
 * @author O.Savinov
 */
public class StationEditPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationEditPostCommand.class);

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

        Station station = new Station.Builder()
                .withId(id)
                .withName(request.getParameter("name"))
                .build();
        try {
            StationManager.getInstance().update(id, station);
            request.getSession().setAttribute("messages", "Station updated!");
            request.getSession().setAttribute("url", "/stations");
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        return "/stations/edit/" + id;
    }

}
