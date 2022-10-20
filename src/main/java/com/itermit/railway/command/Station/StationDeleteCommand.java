package com.itermit.railway.command.Station;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StationDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        StationDAOImpl.getInstance().delete(id);

        request.getSession().setAttribute("messages", "Station deleted!");
        request.getSession().setAttribute("url", "/stations");
        request.setAttribute("action", "delete");

        try {
            response.sendRedirect("/stations");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting /stations!", e);
        }

        return null;
    }

}
