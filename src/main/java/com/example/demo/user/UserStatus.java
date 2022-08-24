package com.example.demo.user;

public class UserStatus {
    String username;
    boolean profileCreated;
    public UserStatus(){}
    public UserStatus(boolean profileCreated, String username) {
        this.profileCreated = profileCreated;
        this.username = username;
    }

    public boolean isProfileCreated() {
        return profileCreated;
    }

    public void setProfileCreated(boolean profileCreated) {
        this.profileCreated = profileCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
