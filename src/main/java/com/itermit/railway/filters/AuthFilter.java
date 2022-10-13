package com.itermit.railway.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebFilter(
        urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = "active", value = "true")}
)
public class AuthFilter implements Filter {

    private Boolean active;
    private Boolean isAuthorized;
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        active = Objects.equals(filterConfig.getInitParameter("active"), "true");
        isAuthorized = false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.trace("#doFilter(servletRequest, servletResponse, filterChain). active: {}", active);

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpSession session = httpReq.getSession();

        if (active) {

            // TODO: вынести в отдельный класс некий, и там получать этот атрибут.
            //  Или, например, кастом таг для этого заюзать.
            //  И для isAdmin тоже таг подойдет.

            isAuthorized = Objects.nonNull(session.getAttribute("isAuthorized"));
            logger.info("httpReq.getRequestURI(): {}", httpReq.getRequestURI());

            if (httpReq.getRequestURI().equals("/profile")) {
                if (!isAuthorized) {
                    logger.info("doFilter is NOT Authorized -> auth.jsp");
                    servletRequest.getRequestDispatcher("/auth.jsp").forward(servletRequest, servletResponse);
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}