package com.cloudslip.facade.model;

public class ApiAccessToken extends BaseEntity {

    private User user;
    private String accessToken;
    private String allowedOrigins = "*";

    public ApiAccessToken() {
    }

    public ApiAccessToken(User user, String accessToken, String allowedOrigins) {
        this.user = user;
        this.accessToken = accessToken;
        this.allowedOrigins = allowedOrigins;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
