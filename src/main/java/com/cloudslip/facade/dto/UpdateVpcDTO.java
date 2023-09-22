package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

public class UpdateVpcDTO extends BaseInputDTO {

    @NotNull
    private ObjectId id;

    @NotNull
    private String name;

    private String dashboardUrl;

    private ObjectId regionId;

    private int totalCPU;

    private int totalMemory;

    private int totalStorage;

    private String bandwidth;

    private boolean autoScalingEnabled;

    private ObjectId companyId;

    public UpdateVpcDTO() {
    }

    public UpdateVpcDTO(@NotNull ObjectId id, @NotNull String name, String dashboardUrl, ObjectId regionId, int totalCPU, int totalMemory, int totalStorage, String bandwidth, boolean autoScalingEnabled) {
        this.id = id;
        this.name = name;
        this.dashboardUrl = dashboardUrl;
        this.regionId = regionId;
        this.totalCPU = totalCPU;
        this.totalMemory = totalMemory;
        this.totalStorage = totalStorage;
        this.bandwidth = bandwidth;
        this.autoScalingEnabled = autoScalingEnabled;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
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

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public ObjectId getRegionId() {
        return regionId;
    }

    public void setRegionId(ObjectId regionId) {
        this.regionId = regionId;
    }

    public int getTotalCPU() {
        return totalCPU;
    }

    public void setTotalCPU(int totalCPU) {
        this.totalCPU = totalCPU;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }

    public int getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(int totalStorage) {
        this.totalStorage = totalStorage;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public boolean isAutoScalingEnabled() {
        return autoScalingEnabled;
    }

    public void setAutoScalingEnabled(boolean autoScalingEnabled) {
        this.autoScalingEnabled = autoScalingEnabled;
    }
}
