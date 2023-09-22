package com.cloudslip.facade.dto;

public class TestOutputDTO extends BaseOutputDTO {

    private String message;

    public TestOutputDTO() {
    }

    public TestOutputDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
