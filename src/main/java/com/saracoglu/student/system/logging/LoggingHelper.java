package com.saracoglu.student.system.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Component
public class LoggingHelper {

    private static final Logger logger = LoggerFactory.getLogger(LoggingHelper.class);

    public void logRequest(HttpServletRequest request) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("Gelen İstek: ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI())
                .append("\nHeaders: ");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            requestLog.append("\n  ").append(headerName).append(": ").append(request.getHeader(headerName));
        }

        logger.info(requestLog.toString());
    }
    public void logResponse(HttpServletResponse response) {
        logger.info("Yanıt Durumu: " + response.getStatus());
    }

    public void logError(String message, Throwable throwable) {
        logger.error("Hata: " + message, throwable);
    }
}
