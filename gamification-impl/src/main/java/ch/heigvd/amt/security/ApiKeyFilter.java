package ch.heigvd.amt.security;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(1)
public class ApiKeyFilter implements Filter {

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (isPublicRessource(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String key = request.getHeader("apiKeyAuth") == null ? "" : request.getHeader("X-API-KEY");
        Optional<ApiKeyEntity> apiKeyEntityOptional = apiKeyRepository.findByValue(key);

        if(apiKeyEntityOptional.isPresent()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String error = "API key is missing or invalid";

            response.reset();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentLength(error.length());
            response.getWriter().write(error);
        }
    }

    /**
     * @param URI
     * @return true if the URI given is a public ressource
     */
    private boolean isPublicRessource(String URI) {
        if (URI.startsWith("/registration"))
            return true;
        return false;
    }

}
