package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class UpdateApplicationDTO {

    private ObjectId applicationId;
    private String name;
    private String packageName;
    private ObjectId teamId;
    private String appCreationType;
    private String applicationType;
    private String applicationBuildType;

    public ObjectId getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(ObjectId applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationBuildType() {
        return applicationBuildType;
    }

    public void setApplicationBuildType(String applicationBuildType) {
        this.applicationBuildType = applicationBuildType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
    }

    public String getAppCreationType() {
        return appCreationType;
    }

    public void setAppCreationType(String appCreationType) {
        this.appCreationType = appCreationType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }
}
