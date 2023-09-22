package com.cloudslip.facade.dto;

import com.cloudslip.facade.model.dummy.GitDirectory;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class UpdateOrganizationDTO implements Serializable {

    private ObjectId id;
    private String name;
    private String description;
    private ObjectId organizationId;
    private GitDirectory gitDirectory;

    public UpdateOrganizationDTO() {
    }

    public UpdateOrganizationDTO(ObjectId id, String name, String description, ObjectId organizationId, GitDirectory gitDirectory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organizationId = organizationId;
        this.gitDirectory = gitDirectory;
    }

    public ObjectId getId() {
        return id;
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

    public GitDirectory getGitDirectory() {
        return gitDirectory;
    }

    public void setGitDirectory(GitDirectory gitDirectory) {
        this.gitDirectory = gitDirectory;
    }
}
