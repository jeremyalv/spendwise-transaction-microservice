package com.spendwise.api.transactionmanagement.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService {
    private String activeJWT = "";
    private final RestTemplate restTemplate;
    private String authVerifyServiceURL = "http://localhost:8081/api/v1/auth/verify";

    @Override
    public ResponseEntity<String> storeJWT(String token) {
        if (token == "") return null;

        this.activeJWT = token;
        System.out.println("===ACTIVE JWT===");
        System.out.println(token);

        return ResponseEntity.ok("Token saved successfully");
    }

    @Override
    public ResponseEntity<String> verify(HttpServletRequest servletRequest) {
        String requestJWT = getJWTFromRequest(servletRequest);
        String url = authVerifyServiceURL;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = new HashMap<>();
            payload.put("token", requestJWT);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            boolean isValid = response.getBody().get("status") == "valid" ? true : false;

            if (!isValid) {
                System.out.println("status: " + response.getBody().get("status"));
                return ResponseEntity.status(401).body("Token Invalid. Access not allowed");
            }

            // Returns the correct status code if token is valid
            return ResponseEntity.status(200).body("Access granted");
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                return ResponseEntity.status(403).body("Client Error. Access not allowed");
            }
        }

        return ResponseEntity.status(403).body("Other Error. Access not allowed");
    }
//    @Override
//    public ResponseEntity<String> verify(HttpServletRequest request) {
//        String jwt = getJWTFromRequest(request);
//        String url = authVerifyServiceURL;
//
//        try {
//            Map<String, String> responseBody = restTemplate.getForObject(url, Map.class);
//
//            String status = responseBody.get("status");
//
//            if (status != "valid") {
//                return ResponseEntity.status(401).body("Access not allowed");
//            }
//
//            // Returns the correct status code if token is valid
//            return ResponseEntity.status(200).body("Access granted");
//        }
//        catch (HttpClientErrorException e) {
//            // Based on Auth service response
//            // TODO '==' may cause bugs
//            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
//                return ResponseEntity.status(403).body("Access not allowed");
//            }
//        }
//
//        return ResponseEntity.status(403).body("Access not allowed");
//    }

    private String getJWTFromRequest(HttpServletRequest request) {
        // get login cookie
        Cookie jwt = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("token")) {
                    jwt = cookie;
                    break;
                }
            }
        }

        return jwt.getValue();
    }
}
