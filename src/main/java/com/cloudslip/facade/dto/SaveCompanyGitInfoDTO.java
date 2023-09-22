package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;
import org.springframework.lang.Nullable;

import java.io.Serializable;


public class SaveCompanyGitInfoDTO implements Serializable {

    private String gitProvider;
    private String username;
    private String apiUrl;
    private String secretKey;
    private ObjectId companyId;

    private String dockerhubId;
    private String dockerhubPassword;

    public SaveCompanyGitInfoDTO() {
    }

    public SaveCompanyGitInfoDTO(String gitProvider, String username, String apiUrl, String secretKey, ObjectId companyId) {
        this.gitProvider = gitProvider;
        this.username = username;
        this.apiUrl = apiUrl;
        this.secretKey = secretKey;
        this.companyId = companyId;
    }

    public String getGitProvider() {
        return gitProvider;
    }

    public void setGitProvider(String gitProvider) {
        this.gitProvider = gitProvider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Nullable
    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(@Nullable ObjectId companyId) {
        this.companyId = companyId;
    }

    public String getDockerhubId() {
        return dockerhubId;
    }

    public void setDockerhubId(String dockerhubId) {
        this.dockerhubId = dockerhubId;
    }

    public String getDockerhubPassword() {
        return dockerhubPassword;
    }

    public void setDockerhubPassword(String dockerhubPassword) {
        this.dockerhubPassword = dockerhubPassword;
    }
}
