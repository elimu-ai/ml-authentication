package org.literacyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Allophone;
import org.literacyapp.dao.AllophoneDao;
import org.literacyapp.dao.GsonToGreenDaoConverter;
import org.literacyapp.dao.Letter;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.dao.Video;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.dao.Word;
import org.literacyapp.dao.WordDao;
import org.literacyapp.model.gson.content.AllophoneGson;
import org.literacyapp.model.gson.content.LetterGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;
import org.literacyapp.model.gson.content.multimedia.VideoGson;
import org.literacyapp.util.ConnectivityHelper;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.JsonLoader;
import org.literacyapp.util.Log;
import org.literacyapp.util.MultimediaLoader;
import org.literacyapp.util.VersionHelper;

import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * 1. Check if Device has already been stored on the server
 * 2. If not, register Device
 * 3. Once registered, download content
 */
public class DownloadContentAlarmReceiver extends BroadcastReceiver {

    public static final String PREF_LAST_CONTENT_SYNC = "pref_last_content_sync";

    private Context context;

    private AllophoneDao allophoneDao;
    private NumberDao numberDao;
    private LetterDao letterDao;
    private WordDao wordDao;
    private VideoDao videoDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass(), "onReceive");

        this.context = context;

        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        allophoneDao = literacyApplication.getDaoSession().getAllophoneDao();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        wordDao = literacyApplication.getDaoSession().getWordDao();
        videoDao = literacyApplication.getDaoSession().getVideoDao();

        boolean isWifiEnabled = ConnectivityHelper.isWifiEnabled(context);
        Log.d(getClass(), "isWifiEnabled: " + isWifiEnabled);
        boolean isWifiConnected = ConnectivityHelper.isWifiConnected(context);
        Log.d(getClass(), "isWifiConnected: " + isWifiConnected);
        if (!isWifiEnabled) {
            Toast.makeText(context, context.getString(R.string.wifi_needs_to_be_enabled), Toast.LENGTH_SHORT).show();
        } else if (!isWifiConnected) {
            Toast.makeText(context, context.getString(R.string.wifi_needs_to_be_connected), Toast.LENGTH_SHORT).show();
        } else {
            // TODO: check if newer version of application exists
            new ReadDeviceAsyncTask().execute();
        }
    }


    private class ReadDeviceAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");

            boolean isServerReachable = ConnectivityHelper.isServerReachable(context);
            Log.d(getClass(), "isServerReachable: " + isServerReachable);
            if (!isServerReachable) {
                return null;
            } else {
                String url = EnvironmentSettings.getRestUrl() + "/device/read/" + DeviceInfoHelper.getDeviceId(context) +
                        "?appVersionCode=" + VersionHelper.getAppVersionCode(context);
                String jsonResponse = JsonLoader.loadJson(url);
                Log.d(getClass(), "jsonResponse: " + jsonResponse);
                return jsonResponse;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            Log.d(getClass(), "onPostExecute");
            super.onPostExecute(jsonResponse);

            if (TextUtils.isEmpty(jsonResponse)) {
                Toast.makeText(context, context.getString(R.string.server_is_not_reachable), Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    if (!"success".equals(jsonObject.getString("result"))) {
                        // Device was not found
                        Toast.makeText(context, context.getString(R.string.registering_device), Toast.LENGTH_SHORT).show();
                        new RegisterDeviceAsyncTask().execute();
                    } else {
                        // Device was found
                        Toast.makeText(context, context.getString(R.string.downloading_content), Toast.LENGTH_SHORT).show();
                        new DownloadContentAsyncTask().execute();
                    }
                } catch (JSONException e) {
                    Log.e(getClass(), null, e);
                    Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private class RegisterDeviceAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");

            boolean isServerReachable = ConnectivityHelper.isServerReachable(context);
            Log.d(getClass(), "isServerReachable: " + isServerReachable);
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
                Log.d(getClass(), "jsonResponse: " + jsonResponse);
                return jsonResponse;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            Log.d(getClass(), "onPostExecute");
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
                        new DownloadContentAsyncTask().execute();
                    }
                } catch (JSONException e) {
                    Log.e(getClass(), null, e);
                    Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private class DownloadContentAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");


            publishProgress("Downloading Allophones");
            String url = EnvironmentSettings.getRestUrl() + "/content/allophone/list" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            String jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    Log.w(getClass(), "Download failed");
                } else {
                    JSONArray jsonArrayAllophones = jsonObject.getJSONArray("allophones");
                    for (int i = 0; i < jsonArrayAllophones.length(); i++) {
                        Type type = new TypeToken<AllophoneGson>(){}.getType();
                        AllophoneGson allophoneGson = new Gson().fromJson(jsonArrayAllophones.getString(i), type);
                        Allophone allophone = GsonToGreenDaoConverter.getAllophone(allophoneGson);
                        Allophone existingAllophone = allophoneDao.queryBuilder()
                                .where(AllophoneDao.Properties.Id.eq(allophone.getId()))
                                .unique();
                        if (existingAllophone == null) {
                            allophoneDao.insert(allophone);
                            Log.d(getClass(), "Stored Allophone with id " + allophone.getId() + " and IPA value /" + allophone.getValueIpa() + "/");
                        } else {
                            Log.d(getClass(), "Allophone /" + allophone.getValueIpa() + "/ already exists in database with id " + allophone.getId());
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(getClass(), null, e);
            }


            publishProgress("Downloading Letters");
            url = EnvironmentSettings.getRestUrl() + "/content/letter/list" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    Log.w(getClass(), "Download failed");
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("letters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Type type = new TypeToken<LetterGson>(){}.getType();
                        LetterGson letterGson = new Gson().fromJson(jsonArray.getString(i), type);
                        Letter letter = GsonToGreenDaoConverter.getLetter(letterGson);
                        Letter existingLetter = letterDao.queryBuilder()
                                .where(LetterDao.Properties.Id.eq(letter.getId()))
                                .unique();
                        if (existingLetter == null) {
                            letterDao.insert(letter);
                            Log.d(getClass(), "Stored Letter with id " + letter.getId() + " and text '" + letter.getText() + "'");
                        } else {
                            Log.d(getClass(), "Letter " + letter.getText() + " already exists in database with id " + letter.getId());
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(getClass(), null, e);
            }


            publishProgress("Downloading Numbers");
            url = EnvironmentSettings.getRestUrl() + "/content/number/list" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    Log.w(getClass(), "Download failed");
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("numbers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Type type = new TypeToken<NumberGson>(){}.getType();
                        NumberGson numberGson = new Gson().fromJson(jsonArray.getString(i), type);
                        Number number = GsonToGreenDaoConverter.getNumber(numberGson);
                        Number existingNumber = numberDao.queryBuilder()
                                .where(NumberDao.Properties.Id.eq(number.getId()))
                                .unique();
                        if (existingNumber == null) {
                            numberDao.insert(number);
                            Log.d(getClass(), "Stored Number with id " + number.getId() + " and value /" + number.getValue() + "/");
                        } else {
                            Log.d(getClass(), "Number " + number.getValue() + " already exists in database with id " + number.getId());
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(getClass(), null, e);
            }


            publishProgress("Downloading Words");
            url = EnvironmentSettings.getRestUrl() + "/content/word/list" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    Log.w(getClass(), "Download failed");
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("words");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Type type = new TypeToken<WordGson>(){}.getType();
                        WordGson wordGson = new Gson().fromJson(jsonArray.getString(i), type);
                        Word word = GsonToGreenDaoConverter.getWord(wordGson);
                        Word existingWord = wordDao.queryBuilder()
                                .where(WordDao.Properties.Id.eq(word.getId()))
                                .unique();
                        if (existingWord == null) {
                            wordDao.insert(word);
                            Log.d(getClass(), "Stored Word with id " + word.getId() + " and text '" + word.getText() + "'");
                        } else {
                            Log.d(getClass(), "Word " + word.getText() + " already exists in database with id " + word.getId());
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(getClass(), null, e);
            }


            // TODO: Audios


            // TODO: Images


            publishProgress("Downloading Videos");
            url = EnvironmentSettings.getRestUrl() + "/content/multimedia/video/list" +
                    "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                    "&locale=" + DeviceInfoHelper.getLocale(context);
            jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (!"success".equals(jsonObject.getString("result"))) {
                    Log.w(getClass(), "Download failed");
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("videos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Type type = new TypeToken<VideoGson>(){}.getType();
                        VideoGson videoGson = new Gson().fromJson(jsonArray.getString(i), type);
                        Video video = GsonToGreenDaoConverter.getVideo(videoGson);
                        Video existingVideo = videoDao.queryBuilder()
                                .where(VideoDao.Properties.Id.eq(video.getId()))
                                .unique();
                        if (existingVideo == null) {
                            // Download bytes
                            byte[] bytes = MultimediaLoader.loadMultimedia(EnvironmentSettings.getBaseUrl() + video.getFileUrl());
                            Log.d(getClass(), "bytes.length: " + bytes.length);
                            video.setBytes(bytes);
                            videoDao.insert(video);
                            Log.d(getClass(), "Stored Video with id " + video.getId() + " and title \"" + video.getTitle() + "\"");
                        } else {
                            Log.d(getClass(), "Video \"" + video.getTitle() + "\" already exists in database with id " + video.getId());
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(getClass(), null, e);
            }


            // Update time of last content synchronization
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putLong(PREF_LAST_CONTENT_SYNC, Calendar.getInstance().getTimeInMillis()).commit();

            String result = "Content download complete. Please restart the application.";
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.d(getClass(), "onProgressUpdate");
            super.onProgressUpdate(values);

            String progressMessage = values[0];
            Log.d(getClass(), "progressMessage: " + progressMessage);
            Toast.makeText(context, progressMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(getClass(), "onPostExecute");
            super.onPostExecute(result);

            Log.d(getClass(), "result: " + result);
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}
