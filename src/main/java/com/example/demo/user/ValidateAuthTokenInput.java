package com.example.demo.user;

public class ValidateAuthTokenInput {
    String username;
    String token;
    public ValidateAuthTokenInput(){}
    public ValidateAuthTokenInput(String username, String token) {
        this.username = username;
        this.token = token;
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
