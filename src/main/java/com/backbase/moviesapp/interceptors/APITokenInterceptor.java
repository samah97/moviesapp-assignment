package com.backbase.moviesapp.interceptors;

import com.backbase.moviesapp.exceptions.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class APITokenInterceptor implements HandlerInterceptor {

    @Value("${app.api.token}")
    private String expectedToken;

    private static final String API_TOKEN_HEADER = "X-API-TOKEN";
    private static final String API_TOKEN_PARAM = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(API_TOKEN_HEADER);
        if (token == null || token.isEmpty()) {
            token = request.getParameter(API_TOKEN_PARAM);
        }
        if (token == null || token.isEmpty()) {
            throw new UnAuthorizedException("API token required");
        }

        if (!expectedToken.equals(token)) {
            throw new UnAuthorizedException("Invalid API token");
        }

        log.info("API token validated");
        return true;
    }
}
