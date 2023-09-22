package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class ReplyMessageDTO implements Serializable {

    private ObjectId messageThreadId;
    private String content;

    public ObjectId getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(ObjectId messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
