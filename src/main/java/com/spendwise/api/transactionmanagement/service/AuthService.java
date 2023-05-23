package com.spendwise.api.transactionmanagement.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> verify(HttpServletRequest request);
}
