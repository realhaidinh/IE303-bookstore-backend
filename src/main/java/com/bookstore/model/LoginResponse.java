package com.bookstore.model;

public class LoginResponse {
    private String accessToken;
    private final String tokenType = "Bearer";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
