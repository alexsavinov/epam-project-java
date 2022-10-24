package com.itermit.railway.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;


@WebFilter(
        urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "encoding", value = "utf-8"),
                @WebInitParam(name = "forceEncoding", value = "true"),
                @WebInitParam(name = "active", value = "true")
        })
public class EncodingFilter implements Filter {
    private Boolean active;
    private String encoding;
    private static final Logger logger = LogManager.getLogger(EncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        active = Objects.equals(filterConfig.getInitParameter("active"), "true");
        encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("#doFilter(servletRequest, servletResponse, filterChain). active: {}", active);

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;

        if (active) {
            if (encoding == null) {
                System.err.println("Cannot get encoding, getting NULL!");
                logger.error("Cannot get encoding, getting NULL!");
                return;
            }

            if (Objects.equals(httpReq.getCharacterEncoding(), encoding)) {
                httpReq.setCharacterEncoding(encoding);
            }
            if (Objects.equals(httpResp.getCharacterEncoding(), encoding)) {
                httpResp.setCharacterEncoding(encoding);
            }

//            logger.info("encoding: {}", encoding);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}