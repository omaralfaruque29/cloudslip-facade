package com.cloudslip.facade.dto;

import com.cloudslip.facade.enums.InitialSettingStatus;

public class UpdateInitialSettingStatusInputDTO extends BaseInputDTO {
    private InitialSettingStatus initialSettingStatus;

    public UpdateInitialSettingStatusInputDTO() {
    }

    public UpdateInitialSettingStatusInputDTO(InitialSettingStatus initialSettingStatus) {
        this.initialSettingStatus = initialSettingStatus;
    }

    public InitialSettingStatus getInitialSettingStatus() {
        return initialSettingStatus;
    }

    public void setInitialSettingStatus(InitialSettingStatus initialSettingStatus) {
        this.initialSettingStatus = initialSettingStatus;
    }
}
