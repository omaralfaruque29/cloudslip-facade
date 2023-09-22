package com.cloudslip.facade.dto;

import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.UserType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import java.util.List;


public class SaveUserDTO extends BaseInputDTO {

    private static final long serialVersionUID = 7954325925563724664L;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private ObjectId companyId;
    private boolean isEnabled;
    private List<Authority> authorities;
    private UserType userType;
    private ObjectId organizationId;
    private List<ObjectId> teamIdList;

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setAuthorities(final List<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
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

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    public boolean hasAuthority(Authority authority) {
        return authorities.contains(authority);
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

    @Transient
    public String getAuthoritiesAsString() {
        String authoritiesStr = "";
        for (Authority authority: authorities) {
            authoritiesStr = authoritiesStr.concat(authority.getAuthority().concat(","));
        }
        if(authoritiesStr.length() > 0) {
            authoritiesStr = authoritiesStr.substring(0, authoritiesStr.length() - 1);
        }
        return authoritiesStr;
    }

    public String toJsonString() {
        return "{" +
                ", \"username\": \"" + username + "\"" +
                ", \"isEnabled\": " + isEnabled +
                ", \"authorities\": " + "\"" + getAuthoritiesAsString() + "\"" +
                "}";
    }
}
