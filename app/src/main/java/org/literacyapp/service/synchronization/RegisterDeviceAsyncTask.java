package org.literacyapp.service.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.literacyapp.R;
import org.literacyapp.util.ConnectivityHelper;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.JsonLoader;
import org.literacyapp.util.VersionHelper;

public class RegisterDeviceAsyncTask extends AsyncTask<Void, Void, String> {

    private Context context;

    public RegisterDeviceAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.i(getClass().getName(), "doInBackground");

        boolean isServerReachable = ConnectivityHelper.isServerReachable(context);
        Log.i(getClass().getName(), "isServerReachable: " + isServerReachable);
        if (!isServerReachable) {
            return null;
        } else {
            String url = EnvironmentSettings.getRestUrl() + "/device/create" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&deviceManufacturer=" + DeviceInfoHelper.getDeviceManufacturer(context) +
                    "&deviceModel=" + DeviceInfoHelper.getDeviceModel(context) +
                    "&deviceSerial=" + DeviceInfoHelper.getDeviceSerialNumber(context) +
                    "&applicationId=" + context.getPackageName() +
                    "&appVersionCode=" + VersionHelper.getAppVersionCode(context) +
                    "&osVersion=" + Build.VERSION.SDK_INT +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            String jsonResponse = JsonLoader.loadJson(url);
            Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
            return jsonResponse;
        }
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        Log.i(getClass().getName(), "onPostExecute");
        super.onPostExecute(jsonResponse);

        if (TextUtils.isEmpty(jsonResponse)) {
            Toast.makeText(context, context.getString(R.string.server_is_not_reachable), Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    // Device was not created
                    Toast.makeText(context, context.getString(R.string.device_registration_failed), Toast.LENGTH_SHORT).show();
                } else {
                    // Device was created
                    Toast.makeText(context, context.getString(R.string.downloading_content), Toast.LENGTH_SHORT).show();
                    new DownloadContentAsyncTask(context).execute();
                }
            } catch (JSONException e) {
                Log.e(getClass().getName(), null, e);
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }
}