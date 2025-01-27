package com.saracoglu.student.system.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")  // Uygulamadaki tüm endpointlere CORS filtresi uygulamak için
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // CORS başlıklarını ekle
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");  // Her yerden erişime izin verir
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");  // Hangi metodların izin verileceği
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization"); // İzin verilen başlıklar
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");  // Çerezlerin gönderilmesine izin verir

        // Eğer OPTIONS isteği yapılmışsa hemen cevap döndür (CORS preflight isteği)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // İstek zincirini devam ettir
        chain.doFilter(request, response);
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        // Filtre başlatılırken yapılacak işlemler varsa buraya eklenebilir
    }

    @Override
    public void destroy() {
        // Filtre yok edilirken yapılacak işlemler varsa buraya eklenebilir
    }
}
