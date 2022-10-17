package com.itermit.railway.command;

import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StationEditPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationEditPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        Station station = new Station.Builder()
                .withId(id)
                .withName(request.getParameter("name"))
                .build();
        StationDAOImpl.getInstance().update(id, station);
        request.getSession().setAttribute("messages", "Station updated!");
        request.getSession().setAttribute("url", "/stations");

        try {
            response.sendRedirect("/stations/edit/" + id);
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
