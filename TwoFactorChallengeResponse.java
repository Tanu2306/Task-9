package com.ecommerce.model.dto;

public class TwoFactorChallengeResponse {

    private boolean twoFactorRequired;
    private String challengeToken;

    public TwoFactorChallengeResponse() {}

    public TwoFactorChallengeResponse(boolean twoFactorRequired, String challengeToken) {
        this.twoFactorRequired = twoFactorRequired;
        this.challengeToken = challengeToken;
    }

    public boolean isTwoFactorRequired() { return twoFactorRequired; }
    public void setTwoFactorRequired(boolean twoFactorRequired) { this.twoFactorRequired = twoFactorRequired; }

    public String getChallengeToken() { return challengeToken; }
    public void setChallengeToken(String challengeToken) { this.challengeToken = challengeToken; }
}
