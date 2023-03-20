package com.rashad.currency.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();

        if (authException.getMessage().equals("Full authentication is required to access this resource"))
            body.put("message", "Bu api-dən istifadə etmək üçün siz öncə login olmalısız. " +
                    "Bunun üçün zəhmət olmasa yuxarda qeyd olunan " +
                    "Login and Register bölməsindən istifadə edin.");

        if (authException.getMessage().equals("Bad credentials"))
            body.put("message", "Daxil etdiyiniz password düzgün deyil");

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
