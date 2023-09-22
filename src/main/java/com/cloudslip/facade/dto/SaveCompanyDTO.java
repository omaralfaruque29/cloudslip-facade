package com.cloudslip.facade.dto;

import java.io.Serializable;

public class SaveCompanyDTO extends BaseInputDTO {

    private String name;
    private String businessEmail;
    private String adminEmail;
    private String website;
    private String address;
    private String phoneNo;
    private String password;

    public SaveCompanyDTO() {
    }

    public SaveCompanyDTO(String name, String businessEmail, String adminEmail, String website, String address, String phoneNo, String password) {
        this.name = name;
        this.businessEmail = businessEmail;
        this.adminEmail = adminEmail;
        this.website = website;
        this.address = address;
        this.phoneNo = phoneNo;
        this.password = password;
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

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
