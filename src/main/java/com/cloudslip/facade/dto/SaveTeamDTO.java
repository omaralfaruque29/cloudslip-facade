package com.cloudslip.facade.dto;

import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class SaveTeamDTO implements Serializable {

    private String name;
    private String description;
    private ObjectId organizationId;
    private User currentUser;

    public SaveTeamDTO() {
    }

    public SaveTeamDTO(String name, String description, ObjectId organizationId) {
        this.name = name;
        this.description = description;
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(ObjectId organizationId) {
        this.organizationId = organizationId;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
