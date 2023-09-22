package com.cloudslip.facade.dto;
import org.bson.types.ObjectId;
import java.util.List;


public class SaveVpcDTO extends BaseInputDTO {

    private String name;

    private ObjectId regionId;

    private int totalCPU;

    private int totalMemory;

    private int totalStorage;

    private String bandwidth;

    private boolean autoScalingEnabled;

    private ObjectId companyId;

    private ObjectId organizationId;

    private List<ObjectId> teamIdList;

    public SaveVpcDTO() {
    }

    public SaveVpcDTO(String name, ObjectId regionId, int totalCPU, int totalMemory, int totalStorage, String bandwidth, boolean autoScalingEnabled) {
        this.name = name;
        this.regionId = regionId;
        this.totalCPU = totalCPU;
        this.totalMemory = totalMemory;
        this.totalStorage = totalStorage;
        this.bandwidth = bandwidth;
        this.autoScalingEnabled = autoScalingEnabled;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
