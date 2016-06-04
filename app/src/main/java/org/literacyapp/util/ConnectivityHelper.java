package org.literacyapp.util;

import android.content.Context;
import android.net.wifi.WifiManager;

public class ConnectivityHelper {

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
}
