package com.cloudslip.facade.enums;

public enum ActionStatus {

    ON_GOING("ON_GOING"),  SUCCESS("SUCCESS"), FAILED("FAILED"), REVERTED("REVERTED");

    // declaring private variable for getting values
    private String value;

    public String getValue()
    {
        return this.value;
    }

    // enum constructor - cannot be public or protected
    private ActionStatus(String value) {
        this.value = value;
    }
}
