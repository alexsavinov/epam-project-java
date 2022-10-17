package com.itermit.railway.command;

import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StationEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        //            try {
//                SendEmailUtil.sendEmail(
//                        "spell477@gmail.com",
//                        "test",
//                        "-->>> testeststestst <<<<---");
//            } catch (MessagingException e) {
//                logger.error("MessagingException e1: {}", e.getMessage());
//                logger.error("MessagingException e2: {}", e);
////                throw new RuntimeException(e);
//            }

        int id = CommandContainer.getIdFromRequest(request);

        Station station = StationDAOImpl.getInstance().get(id);
        request.setAttribute("station", station);
        request.setAttribute("action", "edit");

        try {
            request.getRequestDispatcher("/station.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error station editing! {}", e.getMessage());
            throw new DBException("Error station editing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error station editing! {}", e.getMessage());
            throw new DBException("Error station editing!", e);
        }

        return null;
    }

}
