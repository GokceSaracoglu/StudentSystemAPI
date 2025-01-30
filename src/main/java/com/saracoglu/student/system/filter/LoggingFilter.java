package com.saracoglu.student.system.filter;

import com.saracoglu.student.system.logging.LoggingHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    @Autowired
    private LoggingHelper loggingHelper;  // LoggingHelper sınıfı inject ediliyor

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            loggingHelper.logRequest(httpRequest);
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);
        loggingHelper.logResponse(httpResponse);
    }

    @Override
    public void destroy() {
        System.out.println("LoggingFilter destroyed");
    }
}
