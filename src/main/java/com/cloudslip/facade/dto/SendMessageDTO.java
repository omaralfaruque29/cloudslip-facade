package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;
import java.io.Serializable;
import java.util.List;

public class SendMessageDTO implements Serializable {

    private List<ObjectId> recipientIdList;
    private String subject;
    private String content;

    public List<ObjectId> getRecipientIdList() {
        return recipientIdList;
    }

    public void setRecipientIdList(List<ObjectId> recipientIdList) {
        this.recipientIdList = recipientIdList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
