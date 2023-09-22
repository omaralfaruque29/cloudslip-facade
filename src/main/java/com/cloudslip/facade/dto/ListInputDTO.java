package com.cloudslip.facade.dto;

import java.util.List;

public class ListInputDTO<T> extends BaseInputDTO {

    private List<T> list;

    public ListInputDTO() {
    }

    public ListInputDTO(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
