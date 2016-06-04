package org.literacyapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.literacyapp.dao.DaoMaster;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.JsonToGreenDaoConverter;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.dao.Word;
import org.literacyapp.dao.WordDao;
import org.literacyapp.model.enums.Language;
import org.literacyapp.model.json.NumberJson;
import org.literacyapp.model.json.WordJson;
import org.literacyapp.util.ConnectivityHelper;
import org.literacyapp.util.DeviceIdHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.JsonLoader;
import org.literacyapp.util.Log;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private WordDao wordDao;
    private NumberDao numberDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "literacyapp", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoMaster.createAllTables(db, true);
        daoSession = daoMaster.newSession();
        wordDao = daoSession.getWordDao();
        numberDao = daoSession.getNumberDao();
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

        boolean isWifiEnabled = ConnectivityHelper.isWifiEnabled(this);
        Log.d(getClass(), "isWifiEnabled: " + isWifiEnabled);
        if (isWifiEnabled) {
            // TODO: Check if newer version of application is available for download

            new LoadContentAsyncTask().execute();
        } else {
            // TODO: Check if database is empty. If so, do not continue, and display error message about missing Internet connection.

            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

            finish();
        }
    }

    private class LoadContentAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(getClass(), "doInBackground");

            // Download Words
            String url = EnvironmentSettings.getBaseUrl() + "/rest/word/read" +
                    "?deviceId=" + DeviceIdHelper.getDeviceId(getApplicationContext()) +
                    //"&checksum=" + ...
                    "&language=" + Language.ENGLISH;
            Log.d(getClass(), "url: " + url);
            String jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            if (TextUtils.isEmpty(jsonResponse)) {
                // TODO: handle error
            }
            Type type = new TypeToken<List<WordJson>>(){}.getType();
            List<WordJson> words = new Gson().fromJson(jsonResponse, type);
            Log.d(getClass(), "words.size(): " + words.size());
            for (WordJson wordJson : words) {
                Word word = JsonToGreenDaoConverter.getWord(wordJson);
                List<Word> existingWords = wordDao.queryBuilder()
                        .where(WordDao.Properties.ServerId.eq(word.getServerId()))
                        .list();
                Log.d(getClass(), "existingWords.size(): " + existingWords.size());
                if (existingWords.isEmpty()) {
                    wordDao.insert(word);
                    Log.d(getClass(), "Stored Word in database with id " + word.getId());
                } else {
                    Log.d(getClass(), "Word \"" + word.getText() + "\" already exists in database with id " + word.getId());
                }
            }

            // Download Numbers
            url = EnvironmentSettings.getBaseUrl() + "/rest/number/read" +
                    "?deviceId=" + DeviceIdHelper.getDeviceId(getApplicationContext()) +
                    //"&checksum=" + ...
                    "&language=" + Language.ENGLISH;
            Log.d(getClass(), "url: " + url);
            jsonResponse = JsonLoader.loadJson(url);
            Log.d(getClass(), "jsonResponse: " + jsonResponse);
            if (TextUtils.isEmpty(jsonResponse)) {
                // TODO: handle error
            }
            type = new TypeToken<List<NumberJson>>(){}.getType();
            List<NumberJson> numbers = new Gson().fromJson(jsonResponse, type);
            Log.d(getClass(), "numbers.size(): " + numbers.size());
            for (NumberJson numberJson : numbers) {
                Number number = JsonToGreenDaoConverter.getNumber(numberJson);
                List<Number> existingNumbers = numberDao.queryBuilder()
                        .where(NumberDao.Properties.ServerId.eq(number.getServerId()))
                        .list();
                Log.d(getClass(), "existingNumbers.size(): " + existingNumbers.size());
                if (existingNumbers.isEmpty()) {
                    numberDao.insert(number);
                    Log.d(getClass(), "Stored Number in database with id " + number.getId());
                } else {
                    Log.d(getClass(), "Number \"" + number.getValue() + "\" already exists in database with id " + number.getId());
                }
            }

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
