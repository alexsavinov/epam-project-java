package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class RouteAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteAddGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        try {
            ArrayList<Station> stations = StationManager.getInstance().getAll();
            request.setAttribute("stations", stations);
            request.setAttribute("action", "add");
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        return "/route.jsp";
    }

}
