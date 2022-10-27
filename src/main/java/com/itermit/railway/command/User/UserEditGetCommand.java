package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        try {
            User user = UserManager.getInstance().get(id);
            request.setAttribute("user", user);
            request.setAttribute("action", "edit");
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/user.jsp";
    }

}
