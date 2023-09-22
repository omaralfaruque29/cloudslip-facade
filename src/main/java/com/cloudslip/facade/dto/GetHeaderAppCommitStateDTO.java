package com.cloudslip.facade.dto;

public class GetHeaderAppCommitStateDTO extends BaseInputDTO {

    private String userAgent;
    private String githubDelivery;
    private String githubEvent;
    private String apiAccessToken;
    private String applicationId;

    public GetHeaderAppCommitStateDTO(String userAgent, String githubDelivery, String githubEvent, String apiAccessToken, String applicationId) {
        this.userAgent = userAgent;
        this.githubDelivery = githubDelivery;
        this.githubEvent = githubEvent;
        this.apiAccessToken = apiAccessToken;
        this.applicationId = applicationId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getGithubDelivery() {
        return githubDelivery;
    }

    public void setGithubDelivery(String githubDelivery) {
        this.githubDelivery = githubDelivery;
    }

    public String getGithubEvent() {
        return githubEvent;
    }

    public void setGithubEvent(String githubEvent) {
        this.githubEvent = githubEvent;
    }

    public String getApiAccessToken() {
        return apiAccessToken;
    }

    public void setApiAccessToken(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
