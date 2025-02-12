package com.saracoglu.student.system.filter;

import com.saracoglu.student.system.logging.LoggingHelper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    private final LoggingHelper loggingHelper;

    public LoggingFilter(LoggingHelper loggingHelper) {
        this.loggingHelper = loggingHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = loggingHelper.logRequest(httpRequest);
        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
            long duration = System.currentTimeMillis() - startTime;
            loggingHelper.logResponse(requestId, httpResponse);
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            loggingHelper.logError(requestId, ex.getMessage(), ex);
            throw ex;
        }
    }
}
