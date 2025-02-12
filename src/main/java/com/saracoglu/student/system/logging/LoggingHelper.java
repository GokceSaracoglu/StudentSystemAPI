package com.saracoglu.student.system.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Enumeration;
import java.util.UUID;

@Component
public class LoggingHelper {

    private static final Logger logger = LoggerFactory.getLogger(LoggingHelper.class);

    public String logRequest(HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        StringBuilder requestLog = new StringBuilder();

        requestLog.append("[REQUEST] ID: ").append(requestId)
                .append(" - ").append(request.getMethod())
                .append(" ").append(request.getRequestURI())
                .append("\nHeaders: ");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            requestLog.append("\n  ").append(headerName).append(": ").append(request.getHeader(headerName));
        }

        logger.info(requestLog.toString());
        return requestId;
    }

    public void logResponse(String requestId, HttpServletResponse response) {
        logger.info("[RESPONSE] ID: {} - Status: {}", requestId, response.getStatus());
    }

    public void logError(String requestId, String message, Throwable throwable) {
        logger.error("[ERROR] ID: {} - Hata: {}", requestId, message, throwable);
    }
}
