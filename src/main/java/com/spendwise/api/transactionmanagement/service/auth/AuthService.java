package com.spendwise.api.transactionmanagement.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> storeJWT(String token);
//    ResponseEntity<String> verify();
    ResponseEntity<String> verify(HttpServletRequest request);
}
