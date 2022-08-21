package com.example.demo.user;


import java.time.LocalDateTime;

public class Token {
    String token;
    String tokenType;
    String username;
    String issued;
    String expires;

    public Token() {
    }

    public Token(String token, String tokenType, String username, String issued, String expires) {
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.issued = issued;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}

