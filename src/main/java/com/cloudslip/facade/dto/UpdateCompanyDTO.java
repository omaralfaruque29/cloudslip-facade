package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UpdateCompanyDTO implements Serializable {

    private ObjectId id;
    private String name;
    private String businessEmail;
    private String website;
    private String address;
    private String phoneNo;
    private boolean enabled;

    public UpdateCompanyDTO() {
    }

    public UpdateCompanyDTO(ObjectId id, String name, String businessEmail, String website, String address, String phoneNo, boolean enabled) {
        this.id = id;
        this.name = name;
        this.businessEmail = businessEmail;
        this.website = website;
        this.address = address;
        this.phoneNo = phoneNo;
        this.enabled = enabled;
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

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
