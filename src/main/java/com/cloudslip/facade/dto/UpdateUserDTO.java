package com.cloudslip.facade.dto;

import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.UserType;
import org.bson.types.ObjectId;

import java.util.List;

public class UpdateUserDTO extends BaseInputDTO {

    private ObjectId userId;
    private String email;
    private String firstName;
    private String lastName;
    private ObjectId companyId;
    private boolean isEnabled;
    private List<Authority> authorities;
    private UserType userType;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(ObjectId userId, String email, String firstName, String lastName, ObjectId companyId) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
