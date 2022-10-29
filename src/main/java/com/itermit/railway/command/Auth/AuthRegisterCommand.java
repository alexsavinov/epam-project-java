package com.itermit.railway.command.Auth;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.SendEmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Command to register User.
 *
 * @author O.Savinov
 */
public class AuthRegisterCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthRegisterCommand.class);

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

        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException. Error during setCharacterEncoding!  {}", e.getMessage());
            throw new CommandException("Error set character Encoding!", e);
        }

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String email = request.getParameter("email");

        /* Validation */
        StringBuilder sbErrors = new StringBuilder();
        if (name == null || name.length() < 5) {
            logger.warn("Name incorrect! ");
            sbErrors.append("Name incorrect! ");
        }
        if (email == null || email.length() < 6 || !email.contains("@")) {
            logger.warn("Email incorrect! ");
            sbErrors.append("Email incorrect! ");
        }
        if (password == null || !password.equals(password2)) {
            logger.warn("Password incorrect! ");
            sbErrors.append("Password incorrect! ");
        }

        /* Display errors on the same page */
        if (sbErrors.length() > 0) {
            request.getSession().setAttribute("errors", sbErrors);
            return "/register";
        }

        /* Creating user */
        User user = new User.Builder()
                .withName(name)
                .withPassword(password)
                .withEmail(email)
                .withIsActive(false)
                .withIsAdmin(false)
                .build();

        try {
            user = UserManager.getInstance().get(user);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        if (user.getId() != 0) {
            logger.info("user id: {}", user.getId());

            sbErrors = new StringBuilder();
            if (user.getIsActive()) {
                logger.warn("User {} already activated!", user.getName());
                sbErrors.append("User ").append(user.getName())
                        .append(" already activated! Use login page to enter!");
            } else {
                logger.warn("User exists but not yet activated!");
                sbErrors.append("User already exists and not yet activated! Re-send activation mail?");
            }

            if (sbErrors.length() > 0) {
                request.getSession().setAttribute("errors", sbErrors);
                return "/register";
            }
        }

        user.generateActivateToken();

        try {
            UserManager.getInstance().add(user);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        try {
            SendEmailUtil.sendEmail(
                    email,
                    "Activation email",
                    "You have registered on railway.itermit.com!" +
                            "Please, proceed by following link to complete registratiom:" +
                            "http://localhost:8080/activate/" + user.getActivationToken());
        } catch (MessagingException e) {
            logger.error("Error sending email! {}", e.getMessage());
            throw new CommandException("Error sending email!", e);
        }

        request.getSession().setAttribute("messages",
                "User " + name + " added! Activation email was sent to " + email + "!");

        return "/login";
    }

}
