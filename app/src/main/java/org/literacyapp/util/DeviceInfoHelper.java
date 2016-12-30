package org.literacyapp.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.DeviceDao;
import org.literacyapp.model.Device;
import org.literacyapp.model.enums.Locale;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DeviceInfoHelper {

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceManufacturer(Context context) {
        String deviceManufacturer = "";
        try {
            deviceManufacturer = URLEncoder.encode(Build.MANUFACTURER, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(DeviceInfoHelper.class.getName(), "Build.MANUFACTURER: " + Build.MANUFACTURER, e);
        }
        return deviceManufacturer;
    }

    public static String getDeviceModel(Context context) {
        String deviceModel = "";
        try {
            deviceModel = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(DeviceInfoHelper.class.getName(), "Build.MODEL: " + Build.MODEL, e);
        }
        return deviceModel;
    }

    public static String getDeviceSerialNumber(Context context) {
        String deviceSerial = "";
        try {
            deviceSerial = URLEncoder.encode(Build.SERIAL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(DeviceInfoHelper.class.getName(), "Build.SERIAL: " + Build.SERIAL, e);
        }
        return deviceSerial;
    }

    public static Locale getLocale(Context context) {
        Locale locale = Locale.EN;
        if ("ar".equals(java.util.Locale.getDefault().getLanguage())) {
            locale = Locale.AR;
        } else if ("es".equals(java.util.Locale.getDefault().getLanguage())) {
            locale = Locale.ES;
        } else if ("sw".equals(java.util.Locale.getDefault().getLanguage())) {
            locale = Locale.SW;
        }
        return locale;
    }

    public static Device getDevice(Context context){
        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        DaoSession daoSession = literacyApplication.getDaoSession();

        DeviceDao deviceDao = daoSession.getDeviceDao();
        String deviceId = getDeviceId(context);
        Device device = deviceDao.queryBuilder().where(DeviceDao.Properties.DeviceId.eq(deviceId)).unique();

        if (device == null) {
            device = new Device();
            device.setDeviceId(deviceId);
            deviceDao.insert(device);
        }
        return device;
    }
}
