package com.cloudslip.facade.model;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UserWebSocketSession extends BaseEntity {

    @NotNull
    private ObjectId userId;

    @NotNull
    private String simpSessionId;

    @NotNull
    private String webSocketTopic;


    public UserWebSocketSession() {
    }

    public UserWebSocketSession(@NotNull ObjectId userId, @NotNull String simpSessionId, @NotNull String webSocketTopic) {
        this.userId = userId;
        this.simpSessionId = simpSessionId;
        this.webSocketTopic = webSocketTopic;
        this.setCreateDate(String.valueOf(LocalDateTime.now()));
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getSimpSessionId() {
        return simpSessionId;
    }

    public void setSimpSessionId(String simpSessionId) {
        this.simpSessionId = simpSessionId;
    }

    public String getWebSocketTopic() {
        return webSocketTopic;
    }

    public void setWebSocketTopic(String webSocketTopic) {
        this.webSocketTopic = webSocketTopic;
    }
}
