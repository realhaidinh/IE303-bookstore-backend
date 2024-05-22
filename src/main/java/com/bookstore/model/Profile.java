package com.bookstore.model;

public class Profile {
    String username;
    String role;
    String avatar;
    public Profile(String username, String role, String avatar) {
        this.username = username;
        this.role = role;
        this.avatar = avatar;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
