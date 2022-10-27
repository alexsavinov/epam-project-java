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

public class StationEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        try {
            Station station = StationManager.getInstance().get(id);
            request.setAttribute("station", station);
            request.setAttribute("action", "edit");
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        return "/station.jsp";
    }

}
