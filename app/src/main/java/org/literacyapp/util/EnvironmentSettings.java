package org.literacyapp.util;

import org.literacyapp.model.enums.Environment;

public class EnvironmentSettings {

    public static final String DOMAIN = "literacyapp.org";

//      public static final Environment ENVIRONMENT = Environment.DEV; // Logging active
//      public static final Environment ENVIRONMENT = Environment.TEST; // Logging active
    public static final Environment ENVIRONMENT = Environment.PROD; // Logging deactivated

    public static String getBaseUrl() {
        if (ENVIRONMENT == Environment.DEV) {
            return "http://192.168.1.5:8080/literacyapp-webapp"; // Replace with the IP address of your WIFI router
        } else if (ENVIRONMENT == Environment.TEST) {
            return "http://test." + DOMAIN;
        } else {
            return "http://" + DOMAIN;
        }
    }

    public static String getRestUrl() {
        return getBaseUrl() + "/rest/v1";
    }
}
