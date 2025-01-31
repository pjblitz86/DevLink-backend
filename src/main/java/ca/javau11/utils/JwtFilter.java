package ca.javau11.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

//        String requestURI = httpRequest.getRequestURI();
//        if (requestURI.equals("/api/register") || requestURI.equals("/api/login") ||
//            requestURI.equals("/register") || requestURI.equals("/login") ||
//            requestURI.startsWith("/profiles")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        
        String token = httpRequest.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
        	filterChain.doFilter(request, response);
            return;
        }
        
        token = token.substring(7);

        try {
            String username = JwtUtils.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
