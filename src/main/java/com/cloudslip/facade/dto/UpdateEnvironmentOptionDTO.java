package com.cloudslip.facade.dto;


import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UpdateEnvironmentOptionDTO extends BaseInputDTO {

    @NotNull
    private ObjectId id;

    private boolean isEnabled;

    public UpdateEnvironmentOptionDTO() {
    }

    public UpdateEnvironmentOptionDTO(@NotNull ObjectId id, boolean isEnabled) {
        this.id = id;
        this.isEnabled = isEnabled;
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
}
