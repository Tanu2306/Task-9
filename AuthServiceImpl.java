package com.ecommerce.service.impl;

import com.ecommerce.model.dto.LoginRequest;
import com.ecommerce.model.dto.TokenResponse;
import com.ecommerce.model.dto.TwoFactorChallengeResponse;
import com.ecommerce.model.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.jwt.JwtTokenProvider;
import com.ecommerce.security.service.TotpService;
import com.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TotpService totpService;
    private final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           TotpService totpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.totpService = totpService;
    }

    @Override
    public Object login(LoginRequest request, String ipAddress) {
        User user = userRepository.findByEmailAndTenantId(request.getEmail(), request.getTenantId())
                .orElseThrow(() -> new BadCredentialsException("Invalid email, password, or tenant ID"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email, password, or tenant ID");
        }

        if (totpService.is2FaEnabled(user)) {
            String challengeToken = totpService.generateChallengeToken(user);
            return new TwoFactorChallengeResponse(true, challengeToken);
        }

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        String accessToken = jwtTokenProvider.createAccessToken(authentication, user.getTenantId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getTenantId());

        return new TokenResponse(accessToken, refreshToken, "Bearer");
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (blacklistedTokens.contains(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has already been rotated or invalidated");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getUsername(refreshToken);
        String tenantId = jwtTokenProvider.getTenantId(refreshToken);

        User user = userRepository.findByEmailAndTenantId(email, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User or tenant not found"));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication, user.getTenantId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getTenantId());

        blacklistedTokens.add(refreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken, "Bearer");
    }

    @Override
    public void logout(String token) {
        if (token != null && !token.trim().isEmpty()) {
            blacklistedTokens.add(token);
        }
    }
}
