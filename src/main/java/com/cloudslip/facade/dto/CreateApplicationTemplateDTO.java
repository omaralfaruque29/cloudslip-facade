package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class CreateApplicationTemplateDTO extends BaseInputDTO {
    private ObjectId applicationId;
    private ObjectId companyId;
    private String gitAgentAccessToken;

    public CreateApplicationTemplateDTO() {
    }

    public CreateApplicationTemplateDTO(ObjectId applicationId, ObjectId companyId, String gitAgentAccessToken) {
        this.applicationId = applicationId;
        this.companyId = companyId;
        this.gitAgentAccessToken = gitAgentAccessToken;
    }

    public String getGitAgentAccessToken() {
        return gitAgentAccessToken;
    }

    public void setGitAgentAccessToken(String gitAgentAccessToken) {
        this.gitAgentAccessToken = gitAgentAccessToken;
    }

    public ObjectId getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(ObjectId applicationId) {
        this.applicationId = applicationId;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }
}
