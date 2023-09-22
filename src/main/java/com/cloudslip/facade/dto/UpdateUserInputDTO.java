package com.cloudslip.facade.dto;

import com.cloudslip.facade.enums.Authority;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class UpdateUserInputDTO extends BaseInputDTO {

    private ObjectId id;
    private boolean isEnabled;
    private List<Authority> authorities;

    public UpdateUserInputDTO() {
    }

    public UpdateUserInputDTO(ObjectId id, boolean isEnabled, List<Authority> authorities) {
        this.id = id;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
}
