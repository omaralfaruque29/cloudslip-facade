package com.cloudslip.facade.dto.appissue;

import com.cloudslip.facade.dto.BaseInputDTO;
import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public class CreateAppIssueDTO extends BaseInputDTO {

    private ObjectId applicationId;
    private String title;
    private String description;
    private List<ObjectId> taggedUserIdList;
    private String issueType;
    private String issueStatus;
    private List<User> taggedUserList;

    public CreateAppIssueDTO() {
    }

    public ObjectId getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(ObjectId applicationId) {
        this.applicationId = applicationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ObjectId> getTaggedUserIdList() {
        return taggedUserIdList;
    }

    public void setTaggedUserIdList(List<ObjectId> taggedUserIdList) {
        this.taggedUserIdList = taggedUserIdList;
    }

    public String getIssueType() {
        return issueType;
    }

    public List<User> getTaggedUserList() {
        return taggedUserList;
    }

    public void setTaggedUserList(List<User> taggedUserList) {
        this.taggedUserList = taggedUserList;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }
}
