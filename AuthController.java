package com.ecommerce.controller;

import com.ecommerce.model.dto.LoginRequest;
import com.ecommerce.model.dto.TokenResponse;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.UserService;
import com.ecommerce.security.service.TotpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TotpService totpService;

    public AuthController(AuthService authService, UserService userService, TotpService totpService) {
        this.authService = authService;
        this.userService = userService;
        this.totpService = totpService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        if (request.getTenantId() == null || request.getTenantId().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Object result = authService.login(request, httpRequest != null ? httpRequest.getRemoteAddr() : null);
            return ResponseEntity.ok(result);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body != null ? body.get("refreshToken") : null;
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            TokenResponse tokenResponse = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } catch (IllegalArgumentException | AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            authService.logout(bearerToken.substring(7));
        }
        return ResponseEntity.ok().build();
    }

    public UserService getUserService() {
        return userService;
    }

    public TotpService getTotpService() {
        return totpService;
    }
}
