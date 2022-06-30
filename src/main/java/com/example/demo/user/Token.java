package com.example.demo.user;


import java.time.LocalDateTime;

public class Token {
    String accessToken;
    String tokenType;
    String username;
    LocalDateTime issued;
    LocalDateTime expires;

    public Token(){}
    public Token(String accessToken, String tokenType, String username,LocalDateTime issued, LocalDateTime expires) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.username = username;
        this.issued = issued;
        this.expires = expires;
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

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getIssued() {
        return issued;
    }

    public void setIssued(LocalDateTime issued) {
        this.issued = issued;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }
}
