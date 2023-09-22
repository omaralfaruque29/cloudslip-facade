package com.cloudslip.facade.dto;

import com.cloudslip.facade.dto_helper.AppPipelineStepHelper;
import org.bson.types.ObjectId;

import java.util.List;

public class AddCustomPipelineStepDTO {
    private String name;
    private ObjectId appEnvironmentId;
    private String jenkinsUrl;
    private String jenkinsApiToken;

    public AddCustomPipelineStepDTO() {
    }

    private List<AppPipelineStepHelper> successors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getAppEnvironmentId() {
        return appEnvironmentId;
    }

    public void setAppEnvironmentId(ObjectId appEnvironmentId) {
        this.appEnvironmentId = appEnvironmentId;
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public String getJenkinsApiToken() {
        return jenkinsApiToken;
    }

    public void setJenkinsApiToken(String jenkinsApiToken) {
        this.jenkinsApiToken = jenkinsApiToken;
    }

    public List<AppPipelineStepHelper> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<AppPipelineStepHelper> successors) {
        this.successors = successors;
    }
}
