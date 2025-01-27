package com.saracoglu.student.system.filter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Gelen isteği logla
        System.out.println("Gelen İstek: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

        chain.doFilter(request, response);

        // Giden yanıtı logla
        System.out.println("Yanıt Durumu: " + httpResponse.getStatus());
    }
}
