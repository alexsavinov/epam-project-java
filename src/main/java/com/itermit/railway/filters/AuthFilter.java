package com.itermit.railway.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * Filter for check user permission to page.
 * <p>
 * If user has no access then redirects to Login page.
 *
 * @author O.Savinov
 */
@WebFilter(urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = "active", value = "true")})
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

        logger.debug("#doFilter(servletRequest, servletResponse, filterChain). active: {}", active);

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        HttpSession session = httpReq.getSession();

        if (active.equals(true)) {
            /*
             * Boolean isAuthorized - stored in HttpSession.
             * And it's sets in moment of user login.
             * Then deletes on logout.
             */
            isAuthorized = (Boolean) session.getAttribute("isAuthorized");
            isAuthorized = isAuthorized != null && isAuthorized;

            /* REDIRECTS unauthorized users to login page (no access) */
            if (httpReq.getRequestURI().equals("/profile")
                    || httpReq.getRequestURI().startsWith("/reserves")
                    || httpReq.getRequestURI().startsWith("/users")
                    || httpReq.getRequestURI().startsWith("/routes/add")
                    || httpReq.getRequestURI().startsWith("/routes/edit")
                    || httpReq.getRequestURI().startsWith("/routes/delete")
                    || httpReq.getRequestURI().startsWith("/orders")
                    || httpReq.getRequestURI().startsWith("/stations")) {

                if (isAuthorized.equals(false)) {
                    logger.error("NOT Authorized -> redirecting to /login");
                    httpResp.sendRedirect("/login");
                    return;
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}