package com.cloudslip.facade.dto.VpcGroup;

import org.bson.types.ObjectId;

import java.util.List;

public class UpdateVpcGroupDTO {
    private ObjectId vpcGroupId;
    private String  name;
    private List<ObjectId> vpcIdList;

    public UpdateVpcGroupDTO() {
    }

    public ObjectId getVpcGroupId() {
        return vpcGroupId;
    }

    public void setVpcGroupId(ObjectId vpcGroupId) {
        this.vpcGroupId = vpcGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectId> getVpcIdList() {
        return vpcIdList;
    }

    public void setVpcIdList(List<ObjectId> vpcIdList) {
        this.vpcIdList = vpcIdList;
    }
}
