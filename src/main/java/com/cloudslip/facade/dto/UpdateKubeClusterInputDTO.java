package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class UpdateKubeClusterInputDTO extends BaseInputDTO {

    private ObjectId id;

    private String dashboardUrl;

    private String defaultNamespace;

    private ObjectId regionId;

    private boolean isEnabled;

    private int totalCPU;

    private int totalMemory;

    private int totalStorage;

    private int availableCPU;

    private int availableMemory;

    private int availableStorage;

    public UpdateKubeClusterInputDTO() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    public ObjectId getRegionId() {
        return regionId;
    }

    public void setRegionId(ObjectId regionId) {
        this.regionId = regionId;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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

    public int getAvailableCPU() {
        return availableCPU;
    }

    public void setAvailableCPU(int availableCPU) {
        this.availableCPU = availableCPU;
    }

    public int getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(int availableMemory) {
        this.availableMemory = availableMemory;
    }

    public int getAvailableStorage() {
        return availableStorage;
    }

    public void setAvailableStorage(int availableStorage) {
        this.availableStorage = availableStorage;
    }

    public boolean checkRequiredValidity() {
        if(id == null || defaultNamespace == null || defaultNamespace.isEmpty() || regionId == null ||
                totalCPU <= 0 || totalMemory <= 0 || totalStorage <= 0 ||
                availableCPU < 0 || availableMemory < 0 || availableStorage < 0) {
            return false;
        }
        return true;
    }
}
