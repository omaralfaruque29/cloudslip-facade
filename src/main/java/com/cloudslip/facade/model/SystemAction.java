package com.cloudslip.facade.model;

import com.cloudslip.facade.enums.ActionStatus;
import org.springframework.lang.Nullable;

public class SystemAction extends BaseEntity {
    @Nullable
    private String details;

    private Enum actionStatus = ActionStatus.ON_GOING;

    @Nullable
    private String errorMessage;

    public SystemAction() {
    }

    public SystemAction(String details) {
        this.details = details;
    }

    public SystemAction(String details, Enum actionStatus, String errorMessage) {
        this.details = details;
        this.actionStatus = actionStatus;
        this.errorMessage = errorMessage;
    }

    @Nullable
    public String getDetails() {
        return details;
    }

    public void setDetails(@Nullable String details) {
        this.details = details;
    }

    public Enum getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Enum actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
