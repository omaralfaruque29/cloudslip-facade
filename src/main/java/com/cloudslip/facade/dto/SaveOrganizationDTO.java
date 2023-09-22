package com.cloudslip.facade.dto;

import com.cloudslip.facade.model.dummy.GitDirectory;
import com.cloudslip.facade.model.dummy.GithubOrganization;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class SaveOrganizationDTO implements Serializable {

    private String name;
    private String description;
    private ObjectId companyId;
    private GitDirectory gitDirectory;

    public SaveOrganizationDTO() {
    }

    public SaveOrganizationDTO(String name, String description, ObjectId companyId, GitDirectory gitDirectory) {
        this.name = name;
        this.description = description;
        this.companyId = companyId;
        this.gitDirectory = gitDirectory;
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

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public GitDirectory getGitDirectory() {
        return gitDirectory;
    }

    public void setGitDirectory(GitDirectory gitDirectory) {
        this.gitDirectory = gitDirectory;
    }
}
