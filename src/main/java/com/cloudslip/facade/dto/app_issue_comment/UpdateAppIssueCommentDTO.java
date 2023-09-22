package com.cloudslip.facade.dto.app_issue_comment;

import com.cloudslip.facade.dto.BaseInputDTO;
import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public class UpdateAppIssueCommentDTO extends BaseInputDTO {

    private ObjectId appIssueCommentId;
    private String comment;
    private List<ObjectId> taggedUserIdList;
    private List<User> taggedUserList;

    public UpdateAppIssueCommentDTO() {
    }

    public ObjectId getAppIssueCommentId() {
        return appIssueCommentId;
    }

    public void setAppIssueCommentId(ObjectId appIssueCommentId) {
        this.appIssueCommentId = appIssueCommentId;
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
