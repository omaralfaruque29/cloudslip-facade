package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class UpdateApiAccessTokenDTO extends BaseInputDTO {

    private ObjectId id;
    private String allowedOrigins;

    public UpdateApiAccessTokenDTO() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
