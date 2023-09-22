package com.cloudslip.facade.dto;

import com.cloudslip.facade.model.User;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class UpdateTeamDTO implements Serializable {

    private ObjectId id;
    private String name;
    private String description;
    private ObjectId organizationId;
    private User currentUser;

    public UpdateTeamDTO() {
    }

    public UpdateTeamDTO(ObjectId id, String name, String description, ObjectId organizationId, User currentUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organizationId = organizationId;
        this.currentUser = currentUser;
    }

    public String getId() {
        if(id != null) {
            return id.toHexString();
        }
        return null;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
