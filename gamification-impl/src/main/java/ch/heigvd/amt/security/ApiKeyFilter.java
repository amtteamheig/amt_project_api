package ch.heigvd.amt.security;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class ApiKeyFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String key = request.getHeader("apiKeyAuth") == null ? request.getHeader("apiKeyAuth");
    }
}
