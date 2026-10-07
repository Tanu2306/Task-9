package com.ecommerce.service;

import com.ecommerce.model.dto.LoginRequest;
import com.ecommerce.model.dto.TokenResponse;

public interface AuthService {
    Object login(LoginRequest request, String ipAddress);
    TokenResponse refreshToken(String refreshToken);
    void logout(String token);
}
