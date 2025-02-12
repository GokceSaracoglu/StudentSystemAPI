package com.saracoglu.student.system.filter;

import com.saracoglu.student.system.logging.LoggingHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final long MAX_REQUESTS_PER_MINUTE = 1000;
    private static final ConcurrentHashMap<String, IPRequestData> requestCounts = new ConcurrentHashMap<>();
    private final LoggingHelper loggingHelper;

    public RateLimitFilter(LoggingHelper loggingHelper) {
        this.loggingHelper = loggingHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = loggingHelper.logRequest(request);
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        IPRequestData ipRequestData = requestCounts.computeIfAbsent(clientIp, k -> new IPRequestData(currentTime, 0));
        long elapsedTime = currentTime - ipRequestData.getLastRequestTime();

        if (elapsedTime > TimeUnit.MINUTES.toMillis(1)) {
            ipRequestData.setLastRequestTime(currentTime);
            ipRequestData.setRequestCount(1);
        } else {
            ipRequestData.setRequestCount(ipRequestData.getRequestCount() + 1);
        }

        if (ipRequestData.getRequestCount() > MAX_REQUESTS_PER_MINUTE) {
            loggingHelper.logError(requestId, "Çok fazla istek yapıldı! IP: " + clientIp, null);
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response);
        loggingHelper.logResponse(requestId, response);
    }
}
