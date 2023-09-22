package com.cloudslip.facade.exception.model;


public class ApiErrorException extends RuntimeException {

    private static final long serialVersionUID = -8658131859261427602L;

    private String service;

    public ApiErrorException(final String service) {
        super();
        this.service = service;
    }

    public ApiErrorException(final String message, final String service) {
        super(message);
        this.service = service;
    }

    public ApiErrorException(final String message, final Throwable cause,
                             final String service) {
        super(message, cause);
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }
}
