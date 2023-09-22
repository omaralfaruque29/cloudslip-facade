package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class SetEnvironmentForCompanyDTO implements Serializable {

    private ObjectId companyId;

    @NotNull
    private List<ObjectId> environmentList;

    public SetEnvironmentForCompanyDTO() {
    }

    public SetEnvironmentForCompanyDTO(ObjectId companyId, List<ObjectId> environmentList) {
        this.companyId = companyId;
        this.environmentList = environmentList;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public List<ObjectId> getEnvironmentList() {
        return environmentList;
    }

    public void setEnvironmentList(List<ObjectId> environmentList) {
        this.environmentList = environmentList;
    }
}
