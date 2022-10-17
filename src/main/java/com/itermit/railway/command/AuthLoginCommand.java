package com.itermit.railway.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthLoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        String hex = null;
        hex = passwordEncrypt(password);
        logger.warn("password (SHA-256): {}", hex);

        User user = UserDAOImpl.getInstance().get(
                new User.Builder().withName(name).withPassword(hex).build());

        if (user.getId() == 0) {
            logger.warn("401 Unauthorized");
            request.setAttribute("errors", "401 Unauthorized");
            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                logger.error("IOException. Error redirecting! {}", e.getMessage());
                throw new DBException("Error redirecting!", e);
            }
        } else {
            logger.warn("User logged in: {}", user.getName());
            request.getSession().setAttribute("isAuthorized", true);
            request.getSession().setAttribute("isAdmin", user.getIsAdmin());
            request.getSession().setAttribute("userid", user.getId());
            request.getSession().setAttribute("username", user.getName());
        }
        try {
            response.sendRedirect("/profile");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

    protected static String passwordEncrypt(String password) throws DBException {

        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException. Error encrypting password!  {}", e.getMessage());
            throw new DBException("Error encrypting password!", e);
        }

        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return String.format("%064x", new BigInteger(1, digest));
    }
}
