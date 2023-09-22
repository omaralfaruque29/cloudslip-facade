package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class SaveUserInfoDTO implements Serializable {

    private ObjectId userId;
    private String email;
    private String firstName;
    private String lastName;
    private ObjectId companyId;
    private ObjectId organizationId;
    private List<ObjectId> teamIdList;

    public SaveUserInfoDTO() {
    }

    public SaveUserInfoDTO(ObjectId userId, String email, String firstName, String lastName, ObjectId companyId, ObjectId organizationId, List<ObjectId> teamIdList) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.organizationId = organizationId;
        this.teamIdList = teamIdList;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public ObjectId getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(ObjectId organizationId) {
        this.organizationId = organizationId;
    }

    public List<ObjectId> getTeamIdList() {
        return teamIdList;
    }

    public void setTeamIdList(List<ObjectId> teamIdList) {
        this.teamIdList = teamIdList;
    }
}
