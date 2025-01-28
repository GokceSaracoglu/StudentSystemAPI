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

    private static final long MAX_REQUESTS_PER_MINUTE = 1000;
    private static final ConcurrentHashMap<String, IPRequestData> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        // IPRequestData nesnesi mevcutsa, istek sayısını güncelle
        IPRequestData ipRequestData = requestCounts.get(clientIp);

        if (ipRequestData == null) {
            // Yeni bir IPRequestData nesnesi oluşturuyoruz
            ipRequestData = new IPRequestData(currentTime, 0);
            requestCounts.put(clientIp, ipRequestData);  // Burada IPRequestData'yı ekliyoruz
        }

        // Zaman aşımını kontrol et
        long elapsedTime = currentTime - ipRequestData.getLastRequestTime();

        if (elapsedTime > TimeUnit.MINUTES.toMillis(1)) {
            // Bir dakikadan fazla süre geçtiyse, sayacı sıfırlıyoruz
            ipRequestData.setLastRequestTime(currentTime);
            ipRequestData.setRequestCount(1); // ilk isteği sayıyoruz
        } else {
            // Aksi takdirde, istek sayısını artırıyoruz
            ipRequestData.setRequestCount(ipRequestData.getRequestCount() + 1);
        }

        if (ipRequestData.getRequestCount() > MAX_REQUESTS_PER_MINUTE) {
            // Sınırı aşarsa, 429 (Too Many Requests) hatası döndürüyoruz
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response); // Filtrenin zincirini devam ettir
    }
}
