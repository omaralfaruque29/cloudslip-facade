package com.cloudslip.facade.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

@Component
public class ApplicationProperties {

    @Value("${env.usermanagement-service.base-url}")
    private String USER_MANAGEMENT_SERVICE_BASE_URL;

    @Value("${env.pipeline-service.base-url}")
    private String PIPELINE_SERVICE_BASE_URL;

    @Value("${env.listener-service.base-url}")
    private String LISTENER_SERVICE_BASE_URL;

    @Value("${env.listener-service.api-access-token}")
    private String LISTENER_SERVICE_API_ACCESS_TOKEN;


    public ApplicationProperties() {
    }

    public String getUserManagementServiceBaseUrl() {
        return USER_MANAGEMENT_SERVICE_BASE_URL;
    }

    public String getPipelineServiceBaseUrl() {
        return PIPELINE_SERVICE_BASE_URL;
    }

    public String getListenerServiceBaseUrl() {
        return LISTENER_SERVICE_BASE_URL;
    }

    public String getListenerServiceApiAccessToken() {
        return LISTENER_SERVICE_API_ACCESS_TOKEN;
    }
}
