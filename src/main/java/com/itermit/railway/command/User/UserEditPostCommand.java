package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserEditPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

//        Part filePart = request.getPart("file");
//        String fileName = filePart.getSubmittedFileName();
//
//        InputStream is = filePart.getInputStream();
//
//        String imagesAddress = getServletContext().getRealPath("/images");
//
//        Files.copy(is,
////                    Paths.get(imagesAddress + "/" + fileName),
//                Paths.get("D:/Projects/Java/EPAM/railway.itermit.com/src/main/webapp/images/image.jpg"),
//                StandardCopyOption.REPLACE_EXISTING);

        int id = CommandContainer.getIdFromRequest(request);

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = new User.Builder().withName(name).withPassword(password).build();
        UserDAOImpl.getInstance().update(id, user);
        request.getSession().setAttribute("messages", "User updated!");
        request.getSession().setAttribute("url", "/users");

        try {
            response.sendRedirect("/users");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
