package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class CreateApiAccessTokenDTO extends BaseInputDTO {

    private ObjectId userId;
    private String allowedOrigins;

    public CreateApiAccessTokenDTO() {
    }

    public CreateApiAccessTokenDTO(ObjectId userId, String allowedOrigins) {
        this.userId = userId;
        this.allowedOrigins = allowedOrigins;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
