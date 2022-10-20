package com.itermit.railway.command.Station;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StationAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationAddGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        request.setAttribute("action", "add");

        try {
            request.getRequestDispatcher("/station.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error station adding! {}", e.getMessage());
            throw new DBException("Error station adding!", e);
        } catch (IOException e) {
            logger.error("IOException. Error station adding! {}", e.getMessage());
            throw new DBException("Error station adding!", e);
        }

        return null;
    }

}
