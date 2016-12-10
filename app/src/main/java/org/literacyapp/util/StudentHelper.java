package org.literacyapp.util;

public class StudentHelper {

    public static String extractDeviceIdFromUniqueId(String uniqueId) {
        String[] uniqueIdArray = uniqueId.split("_");
        String deviceId = uniqueIdArray[0];
        return deviceId;
    }

    public static Long extractLongIdFromUniqueId(String uniqueId) {
        String[] uniqueIdArray = uniqueId.split("_");
        Long longId = Long.valueOf(uniqueIdArray[1]);
        return longId;
    }
}
