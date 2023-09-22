package com.cloudslip.facade.dto.VpcGroup;

import org.bson.types.ObjectId;

import java.util.List;

public class CreateVpcGroupDTO {
    private ObjectId companyId;
    private String  name;
    private List<ObjectId> vpcIdList;

    public CreateVpcGroupDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public List<ObjectId> getVpcIdList() {
        return vpcIdList;
    }

    public void setVpcIdList(List<ObjectId> vpcIdList) {
        this.vpcIdList = vpcIdList;
    }
}
