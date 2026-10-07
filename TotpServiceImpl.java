package com.ecommerce.security.service;

import com.ecommerce.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TotpServiceImpl implements TotpService {

    @Override
    public boolean is2FaEnabled(User user) {
        return false;
    }

    @Override
    public String generateChallengeToken(User user) {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean verifyTotp(String challengeToken, String code) {
        return code != null && !code.trim().isEmpty();
    }
}
