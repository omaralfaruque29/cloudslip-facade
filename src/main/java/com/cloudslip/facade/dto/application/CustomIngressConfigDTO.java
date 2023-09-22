package com.cloudslip.facade.dto.application;

import org.bson.types.ObjectId;

public class CustomIngressConfigDTO {
    private ObjectId vpcId;
    private ObjectId environmentId;
    private String customIngress;

    public ObjectId getVpcId() {
        return vpcId;
    }

    public ObjectId getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(ObjectId environmentId) {
        this.environmentId = environmentId;
    }

    public void setVpcId(ObjectId vpcId) {
        this.vpcId = vpcId;
    }

    public String getCustomIngress() {
        return customIngress;
    }

    public void setCustomIngress(String customIngress) {
        this.customIngress = customIngress;
    }
}
