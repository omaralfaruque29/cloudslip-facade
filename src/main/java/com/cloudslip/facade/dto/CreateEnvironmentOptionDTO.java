package com.cloudslip.facade.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CreateEnvironmentOptionDTO extends BaseInputDTO {

    @NotNull
    private String name;

    @NotNull
    private String shortName;

    private String description;

    public CreateEnvironmentOptionDTO() {
    }

    public CreateEnvironmentOptionDTO(String name, String shortName, String description) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
