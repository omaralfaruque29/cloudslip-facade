package com.cloudslip.facade.model;

import java.io.Serializable;

public class GitInfo implements Serializable {

    private String gitProvider;
    private String apiUrl;
    private String username;
    private String secretKey;

    public GitInfo() {
    }

    public GitInfo(String gitProvider, String apiUrl, String username, String secretKey) {
        this.gitProvider = gitProvider;
        this.apiUrl = apiUrl;
        this.username = username;
        this.secretKey = secretKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGitProvider() {
        return gitProvider;
    }

    public void setGitProvider(String gitProvider) {
        this.gitProvider = gitProvider;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
