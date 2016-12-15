package org.literacyapp.service.synchronization;

import android.content.Context;
import android.os.AsyncTask;
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

public class ReadDeviceAsyncTask extends AsyncTask<Void, Void, String> {

    private Context context;

    public ReadDeviceAsyncTask(Context context) {
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
            String url = EnvironmentSettings.getRestUrl() + "/device/read/" + DeviceInfoHelper.getDeviceId(context) +
                    "?appVersionCode=" + VersionHelper.getAppVersionCode(context);
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
            Toast.makeText(context, context.getString(R.string.server_is_not_reachable), Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    // Device was not found
//                    Toast.makeText(context, context.getString(R.string.registering_device), Toast.LENGTH_SHORT).show();
                    Log.i(getClass().getName(), context.getString(R.string.registering_device));
                    new RegisterDeviceAsyncTask(context).execute();
                } else {
                    // Device was found
//                    Toast.makeText(context, context.getString(R.string.downloading_content), Toast.LENGTH_SHORT).show();
                    Log.i(getClass().getName(), context.getString(R.string.downloading_content));
                    new DownloadContentAsyncTask(context).execute();
                }
            } catch (JSONException e) {
                Log.e(getClass().getName(), null, e);
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }
}