package com.example.demo.user;

public class ProfileCreatedInput {
    boolean isProfileCreated;
    String username;
    String token;

    public ProfileCreatedInput(){}
    public ProfileCreatedInput(boolean isProfileCreated, String username, String token) {
        this.isProfileCreated = isProfileCreated;
        this.username = username;
        this.token = token;
    }

    public boolean isProfileCreated() {
        return isProfileCreated;
    }

    public void setProfileCreated(boolean profileCreated) {
        isProfileCreated = profileCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
