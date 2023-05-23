package com.spendwise.api.transactionmanagement.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService {
    private final RestTemplate restTemplate;
    private String authVerifyServiceURL = "http://localhost:8081/api/v1/auth/verify";

    @Override
    public ResponseEntity<String> verify(HttpServletRequest request) {
        Cookie jwtCookie = getJWTCookie(request);
        String jwt = getJWTFromCookie(jwtCookie);
        String url = authVerifyServiceURL;

        try {
            Map<String, String> responseBody = restTemplate.getForObject(url, Map.class);

            String status = responseBody.get("status");

            if (status != "valid") {
                return ResponseEntity.status(401).body("Access not allowed");
            }

            // Returns the correct status code if token is valid
            return ResponseEntity.status(200).body("Access granted");
        }
        catch (HttpClientErrorException e) {
            // Based on Auth service response
            // TODO '==' may cause bugs
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                return ResponseEntity.status(403).body("Access not allowed");
            }
        }

        return ResponseEntity.status(403).body("Access not allowed");
    }
    private String getJWTFromCookie(Cookie jwt) {
        return jwt.getValue();
    }

    private Cookie getJWTCookie(HttpServletRequest request) {
        // get login cookie
        Cookie loginCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("token")) {
                    loginCookie = cookie;
                    break;
                }
            }
        }

        return loginCookie;
    }
}
