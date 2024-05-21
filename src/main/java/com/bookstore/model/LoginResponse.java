package com.bookstore.model;

public class LoginResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String role;
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String avatar;
    public LoginResponse(String accessToken, String username, String role, String avatar) {
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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
