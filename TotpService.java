package com.ecommerce.security.service;

import com.ecommerce.model.entity.User;

public interface TotpService {
    boolean is2FaEnabled(User user);
    String generateChallengeToken(User user);
    boolean verifyTotp(String challengeToken, String code);
}
