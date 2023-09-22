package com.cloudslip.facade.constant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class ApplicationConstant {

    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_DEV = "ROLE_ADMIN";
    public static final String ROLE_OPS = "ROLE_OPS";

    private static final Hashtable<String, Boolean> allowedUrlsByApiAccessToken = new Hashtable<String, Boolean>(){{
        put("/access-token-test", true);
        put("/web-socket/**", true);
        put("/api/app-commit-state/generate-state", true);
        put("/api/app-pipeline-step/update-status", true);
    }};

    public static Hashtable<String, Boolean> getAllowedUrlsByApiAccessToken() {
        return allowedUrlsByApiAccessToken;
    }

    public static List<String> getAllowedUrlAsList() {
        Set<String> keySet = allowedUrlsByApiAccessToken.keySet();
        return new ArrayList<String>(keySet);
    }
}
