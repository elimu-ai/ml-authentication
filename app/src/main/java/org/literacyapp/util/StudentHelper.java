package org.literacyapp.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.literacyapp.dao.StudentDao;

import java.io.File;

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

    public static String generateNextUniqueId(Context context, StudentDao studentDao) {
        Log.i(StudentHelper.class.getName(), "generateNextUniqueId");

        String deviceId = DeviceInfoHelper.getDeviceId(context);
        Log.i(StudentHelper.class.getName(), "Looking up the number of Students registered on device " + deviceId);
        long count = studentDao.queryBuilder()
                .where(StudentDao.Properties.UniqueId.like(deviceId + "%"))
                .count();
        Log.i(StudentHelper.class.getName(), "count: " + count);
        String uniqueId = deviceId + "_" + (count + 1);
        Log.i(StudentHelper.class.getName(), "uniqueId: " + uniqueId);
        return uniqueId;
    }

    public static File getStudentDirectory() {
        File studentDirectory = new File(Environment.getExternalStorageDirectory() + "/.literacyapp/student");
        if (!studentDirectory.exists()) {
            studentDirectory.mkdirs();
        }
        return studentDirectory;
    }

    public static File getTestStudentsDirectory() {
        File testStudentsDirectory = new File(getStudentDirectory(), "test_students");
        if (!testStudentsDirectory.exists()) {
            testStudentsDirectory.mkdir();
        }
        return testStudentsDirectory;
    }

    public static File getStudentImageDirectory() {
        File studentImageDirectory = new File(getStudentDirectory(), "student_image");
        if (!studentImageDirectory.exists()) {
            studentImageDirectory.mkdir();
        }
        return studentImageDirectory;
    }

    public static File getStudentAvatarDirectory() {
        File studentThumbnailDirectory = new File(getStudentDirectory(), "student_avatar");
        if (!studentThumbnailDirectory.exists()) {
            studentThumbnailDirectory.mkdir();
        }
        return studentThumbnailDirectory;
    }
}
