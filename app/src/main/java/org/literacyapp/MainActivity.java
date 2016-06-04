package org.literacyapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.literacyapp.deviceadmin.DeviceAdmin;
import org.literacyapp.util.DeviceIdHelper;
import org.literacyapp.util.Log;

import edu.cmu.pocketsphinx.demo.PocketSphinxActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onCreate");
        super.onStart();

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        Log.d(getClass(), "configurationInfo.getGlEsVersion(): " + configurationInfo.getGlEsVersion());
        Log.d(getClass(), "configurationInfo.reqGlEsVersion: " + configurationInfo.reqGlEsVersion);

        String deviceId = DeviceIdHelper.getDeviceId(this);
        Log.d(getClass(), "deviceId: " + deviceId);

        new LoadContentAsyncTask().execute();
    }

    private class LoadContentAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");

            // Download updated content from server
            final String url = EnvironmentSettings.getBaseUrl() + "/rest/v2/wordevents/read/adjectives" +
                    "?email=" + UserPrefsHelper.getUserProfileJson(this).getEmail() +
                    "&checksum=" + RestSecurityHelper.getChecksum(UserPrefsHelper.getUserProfileJson(this).getEmail());;
            Log.d(getClass().getName(), "url: " + url);
            String requestBody = null;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(getClass().getName(), "onResponse, response: " + response);
                            try {
                                if ("error".equals(response.getString("result"))) {
//                                    Log.e(getClass().getName(), "url: " + url, new Exception(response.getString("details")));
                                    Log.w(getClass().getName(), "url: " + url + ", details: " + response.getString("details"));
                                    Toast.makeText(getApplicationContext(), "details: " + response.getString("details"), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                Type type = new TypeToken<List<WordEventJson>>(){}.getType();
                                wordAdjectiveEventsPendingRevision = new Gson().fromJson(response.getString("wordEvents"), type);
                                Log.d(getClass().getName(), "wordAdjectiveEventsPendingRevision: " + wordAdjectiveEventsPendingRevision);
                                originalListSize = wordAdjectiveEventsPendingRevision.size();
                            } catch (JSONException e) {
                                Log.e(getClass().getName(), "url: " + url, e);
                                Toast.makeText(getApplicationContext(), "exception: " + e.getClass().getName(), Toast.LENGTH_LONG).show();
                                GoogleAnalyticsHelper.trackEvent(getApplicationContext(), "vocabulary_revision", "vocabulary_read_adjectives_exception", e.getClass().getName() + "_" + e.getMessage() + "_" + url);
                            }

                            openNextCardInList();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(getClass().getName(), "onErrorResponse, url: " + url, error);
                            Toast.makeText(getApplicationContext(), "error: " + error.getClass().getName(), Toast.LENGTH_LONG).show();
                            GoogleAnalyticsHelper.trackEvent(getApplicationContext(), "vocabulary_revision", "vocabulary_read_adjectives_volleyerror", error.getClass().getName() + "_" + error.getMessage() + "_" + url);
                        }
                    }
            );
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            ApplicationController.getInstance().getRequestQueue().add(jsonObjectRequest);


            // Store in database
            // TODO

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(getClass(), "onPostExecute");
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

            finish();
        }
    }
}
