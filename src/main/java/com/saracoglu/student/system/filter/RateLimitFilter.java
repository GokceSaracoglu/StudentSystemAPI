package com.saracoglu.student.system.filter;

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

    private static final long MAX_REQUESTS_PER_MINUTE = 100;
    private static final ConcurrentHashMap<String, Long> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(clientIp, currentTime);

        long elapsedTime = currentTime - requestCounts.get(clientIp);

        if (elapsedTime > TimeUnit.MINUTES.toMillis(1)) {
            requestCounts.put(clientIp, currentTime); // Yeniden başlat
        } else if (requestCounts.merge(clientIp, 1L, Long::sum) > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429); // HTTP 429: Too Many Requests
            response.getWriter().write("Too many requests");
            return;

        }

        filterChain.doFilter(request, response);
    }
}
