package org.literacyapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionHelper {

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersionCode(Context context) {
        Log.d(VersionHelper.class, "getAppVersionCode");

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
