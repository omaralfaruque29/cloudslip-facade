package com.cloudslip.facade.dto;

public class GenerateUserFromUserInfoInputDTO extends BaseInputDTO {

    private ResponseDTO userInfoResponse;

    public GenerateUserFromUserInfoInputDTO() {
    }

    public GenerateUserFromUserInfoInputDTO(ResponseDTO userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }

    public ResponseDTO getUserInfoResponse() {
        return userInfoResponse;
    }

    public void setUserInfoResponse(ResponseDTO userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }
}
