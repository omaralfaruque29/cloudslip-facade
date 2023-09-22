package com.cloudslip.facade.dto;

import org.springframework.data.domain.Pageable;

public class GenerateUserListFromUserInfoListInputDTO extends BaseInputDTO {

    private GetListFilterInput listFilterInput;
    private ResponseDTO userInfoListResponse;
    private Pageable pageable;

    public GenerateUserListFromUserInfoListInputDTO() {
    }

    public GenerateUserListFromUserInfoListInputDTO(GetListFilterInput listFilterInput, ResponseDTO userInfoListResponseDTO, Pageable pageable) {
        this.listFilterInput = listFilterInput;
        this.userInfoListResponse = userInfoListResponseDTO;
        this.pageable = pageable;
    }

    public GetListFilterInput getListFilterInput() {
        return listFilterInput;
    }

    public void setListFilterInput(GetListFilterInput listFilterInput) {
        this.listFilterInput = listFilterInput;
    }

    public ResponseDTO getUserInfoListResponse() {
        return userInfoListResponse;
    }

    public void setUserInfoListResponse(ResponseDTO userInfoListResponse) {
        this.userInfoListResponse = userInfoListResponse;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
