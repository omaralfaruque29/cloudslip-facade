package com.cloudslip.facade.dto.app_issue_comment;

import com.cloudslip.facade.dto.BaseInputDTO;
import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public class AddAppIssueCommentDTO extends BaseInputDTO {

    private ObjectId appIssueId;
    private ObjectId parentAppIssueCommentId;
    private String comment;
    private List<ObjectId> taggedUserIdList;
    private List<User> taggedUserList;

    public ObjectId getAppIssueId() {
        return appIssueId;
    }

    public void setAppIssueId(ObjectId appIssueId) {
        this.appIssueId = appIssueId;
    }

    public ObjectId getParentAppIssueCommentId() {
        return parentAppIssueCommentId;
    }

    public void setParentAppIssueCommentId(ObjectId parentAppIssueCommentId) {
        this.parentAppIssueCommentId = parentAppIssueCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ObjectId> getTaggedUserIdList() {
        return taggedUserIdList;
    }

    public void setTaggedUserIdList(List<ObjectId> taggedUserIdList) {
        this.taggedUserIdList = taggedUserIdList;
    }

    public List<User> getTaggedUserList() {
        return taggedUserList;
    }

    public void setTaggedUserList(List<User> taggedUserList) {
        this.taggedUserList = taggedUserList;
    }
}
