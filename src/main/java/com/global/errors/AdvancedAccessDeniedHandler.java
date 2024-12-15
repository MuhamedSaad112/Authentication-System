package com.global.errors;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdvancedAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = Logger.getLogger(AdvancedAccessDeniedHandler.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        
        String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "Unknown User";
        String requestedUrl = request.getRequestURI();
        
        logger.warning("Access Denied: User " + username + " tried to access " + requestedUrl);

        sendAccessDeniedAlert(username, requestedUrl);
        
        response.setStatus(HttpStatus.FORBIDDEN.value());
        
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{\n" +
                "\"error\": \"Access Denied\",\n" +
                "\"message\": \"You do not have permission to access this resource.\",\n" +
                "\"requestedUrl\": \"" + requestedUrl + "\",\n" +
                "\"user\": \"" + username + "\",\n" +
                "\"status\": 403,\n" +
                "\"timestamp\": \"" + System.currentTimeMillis() + "\"\n" +
                "}");
        writer.flush();
    }

    private void sendAccessDeniedAlert(String username, String requestedUrl) {
        logger.info("Sending alert for access denied attempt: User: " + username + ", URL: " + requestedUrl);
    }
}
