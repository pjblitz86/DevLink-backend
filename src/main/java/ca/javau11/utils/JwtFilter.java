package ca.javau11.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class JwtFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
	        throws IOException, ServletException {

	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpServletResponse httpResponse = (HttpServletResponse) response;

	    String token = httpRequest.getHeader("Authorization");

	    if (token != null && token.startsWith("Bearer ")) {
	        token = token.substring(7);

	        try {
	            if (!JwtUtils.isTokenExpired(token)) {
	                JwtUtils.validateToken(token);
	                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
	                return;
	            }
	        } catch (Exception e) {
	            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
	            return;
	        }
	    }

	    filterChain.doFilter(request, response);
	}


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
