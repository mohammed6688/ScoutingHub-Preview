package com.krnal.products.scoutinghub.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing Logging Filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Request URI: {}", httpRequest.getRequestURI());
        logger.info("Request Method: {}", httpRequest.getMethod());
        logger.info("Authorization Header: {}", httpRequest.getHeader("Authorization"));

        chain.doFilter(request, response);

        logger.info("Response Status: {}", httpResponse.getStatus());
    }

    @Override
    public void destroy() {
        logger.info("Destroying Logging Filter");
    }
}