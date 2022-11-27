package com.itermit.railway.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Localization filter for user messages.
 *
 * @author O.Savinov
 */
@WebFilter(urlPatterns = {"/*"})
public class LocalizationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(LocalizationFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("#doFilter(servletRequest, servletResponse, filterChain).");

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String currentLocale = (String) httpReq.getSession().getAttribute("currentLocale");

        HashMap localizedMessages = (HashMap) httpReq.getSession().getAttribute("localizedMessages");
        if (localizedMessages != null) {
            httpReq.setAttribute("messages", localizedMessages.get(currentLocale));
        }

        HashMap localizedErrors = (HashMap) httpReq.getSession().getAttribute("localizedErrors");
        if (localizedErrors != null) {
            httpReq.setAttribute("errors", localizedErrors.get(currentLocale));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}