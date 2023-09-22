package com.cloudslip.facade.dto;

import org.bson.types.ObjectId;

public class UpdateRegionInputDTO extends BaseInputDTO {

    private ObjectId id;

    private String name;

    private String description;

    public UpdateRegionInputDTO() {
    }

    public ObjectId getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
