package com.saracoglu.student.system.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LoggingFilter implements Filter {

    // Toplam istek sayısını tutan sayaç
    private final AtomicLong totalRequestCount = new AtomicLong(0);

    // Kategoriye göre istek sayısını tutan harita
    private final ConcurrentHashMap<String, AtomicLong> requestCategoryCounts = new ConcurrentHashMap<>();

    // Yeni endpointler için sayıcılar
    private final ConcurrentHashMap<String, AtomicLong> endpointRequestCounts = new ConcurrentHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // İstek sayısını artır
            totalRequestCount.incrementAndGet();

            // Kategoriye göre istekleri artır
            String category = categorizeRequest(httpRequest);
            requestCategoryCounts
                    .computeIfAbsent(category, key -> new AtomicLong(0))
                    .incrementAndGet();

            // Yeni endpointler için sayıcıları artır
            String endpoint = httpRequest.getRequestURI();
            endpointRequestCounts
                    .computeIfAbsent(endpoint, key -> new AtomicLong(0))
                    .incrementAndGet();

            System.out.println("Gelen İstek: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        }

        chain.doFilter(request, response); // Zincirin bir sonraki filtresine geç
    }

    @Override
    public void destroy() {
        System.out.println("LoggingFilter destroyed");
    }

    /**
     * İsteklerin kategorilere ayrılması için basit bir yöntem.
     * Örneğin, API path'e göre kategorilendirme yapılabilir.
     */
    private String categorizeRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api/authenticate") || uri.startsWith("/api/register")) {
            return "Authentication";
        } else if (uri.startsWith("/api/student")) {
            return "Student Operations";
        } else if (uri.startsWith("/api/admin")) {
            return "Admin Operations";
        } else {
            return "Other";
        }
    }

    /**
     * Toplam istek sayısını döner.
     */
    public long getTotalRequestCount() {
        return totalRequestCount.get();
    }

    /**
     * Kategori bazında istek sayılarını döner.
     */
    public ConcurrentHashMap<String, AtomicLong> getRequestCategoryCounts() {
        return requestCategoryCounts;
    }

    /**
     * Her endpoint için istek sayılarını döner.
     */
    public ConcurrentHashMap<String, AtomicLong> getEndpointRequestCounts() {
        return endpointRequestCounts;
    }

    /**
     * Her dakika istek istatistiklerini loglar.
     */
    @Scheduled(fixedRate = 60000) // Her dakika çalıştır
    public void logRequestStatistics() {
        // Genel istatistikler
        System.out.println("Toplam İstek Sayısı: " + totalRequestCount.get());
        requestCategoryCounts.forEach((category, count) -> {
            System.out.println("Kategori: " + category + ", İstek Sayısı: " + count.get());
        });

        // Yeni endpoint istatistikleri
        endpointRequestCounts.forEach((endpoint, count) -> {
            System.out.println("Endpoint: " + endpoint + ", İstek Sayısı: " + count.get());
        });
    }
}
