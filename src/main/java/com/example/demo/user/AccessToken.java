package com.example.demo.user;

public class AccessToken {
    String username;
    String accessToken;
    String refreshToken;
    String issued;
    String expires;
    String tokenType;
    public AccessToken(){}
    public AccessToken(String username, String accessToken, String refreshToken, String issued, String expires, String tokenType) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.issued = issued;
        this.expires = expires;
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
