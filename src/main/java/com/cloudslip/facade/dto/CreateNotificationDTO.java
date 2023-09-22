package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class CreateNotificationDTO implements Serializable {

    private String text;
    private String type;
    private String email;

    public CreateNotificationDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
