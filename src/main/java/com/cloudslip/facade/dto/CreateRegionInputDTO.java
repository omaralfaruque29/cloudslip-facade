package com.cloudslip.facade.dto;

public class CreateRegionInputDTO extends BaseInputDTO {

    private String name;

    private String description;

    public CreateRegionInputDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
