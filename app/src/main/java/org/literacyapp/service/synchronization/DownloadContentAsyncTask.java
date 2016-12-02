package org.literacyapp.service.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.AllophoneDao;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.GsonToGreenDaoConverter;
import org.literacyapp.dao.ImageDao;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.dao.WordDao;
import org.literacyapp.model.content.Allophone;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.Word;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.model.content.multimedia.Image;
import org.literacyapp.model.content.multimedia.Video;
import org.literacyapp.model.gson.content.AllophoneGson;
import org.literacyapp.model.gson.content.LetterGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;
import org.literacyapp.model.gson.content.multimedia.AudioGson;
import org.literacyapp.model.gson.content.multimedia.ImageGson;
import org.literacyapp.model.gson.content.multimedia.VideoGson;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.JsonLoader;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.MultimediaLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class DownloadContentAsyncTask extends AsyncTask<Void, String, String> {

    private Context context;

    private AllophoneDao allophoneDao;
    private NumberDao numberDao;
    private LetterDao letterDao;
    private WordDao wordDao;
    private AudioDao audioDao;
    private ImageDao imageDao;
    private VideoDao videoDao;

    public DownloadContentAsyncTask(Context context) {
        this.context = context;

        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        allophoneDao = literacyApplication.getDaoSession().getAllophoneDao();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        wordDao = literacyApplication.getDaoSession().getWordDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
        imageDao = literacyApplication.getDaoSession().getImageDao();
        videoDao = literacyApplication.getDaoSession().getVideoDao();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.i(getClass().getName(), "doInBackground");


        publishProgress("Downloading Allophones");
        String url = EnvironmentSettings.getRestUrl() + "/content/allophone/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        String jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
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
                        Log.i(getClass().getName(), "Stored Allophone with id " + allophone.getId() + " and IPA value /" + allophone.getValueIpa() + "/");
                    } else {
                        Log.i(getClass().getName(), "Allophone /" + allophone.getValueIpa() + "/ already exists in database with id " + allophone.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Letters");
        url = EnvironmentSettings.getRestUrl() + "/content/letter/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
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
                        Log.i(getClass().getName(), "Stored Letter with id " + letter.getId() + " and text '" + letter.getText() + "'");
                    } else {
                        Log.i(getClass().getName(), "Letter " + letter.getText() + " already exists in database with id " + letter.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Numbers");
        url = EnvironmentSettings.getRestUrl() + "/content/number/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
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
                        Log.i(getClass().getName(), "Stored Number with id " + number.getId() + " and value /" + number.getValue() + "/");
                    } else {
                        Log.i(getClass().getName(), "Number " + number.getValue() + " already exists in database with id " + number.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Words");
        url = EnvironmentSettings.getRestUrl() + "/content/word/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
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
                        Log.i(getClass().getName(), "Stored Word with id " + word.getId() + " and text '" + word.getText() + "'");
                    } else {
                        Log.i(getClass().getName(), "Word " + word.getText() + " already exists in database with id " + word.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }



        publishProgress("Downloading Audios");
        url = EnvironmentSettings.getRestUrl() + "/content/multimedia/audio/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("audios");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Type type = new TypeToken<AudioGson>(){}.getType();
                    AudioGson audioGson = new Gson().fromJson(jsonArray.getString(i), type);
                    Audio audio = GsonToGreenDaoConverter.getAudio(audioGson);
                    Audio existingAudio = audioDao.queryBuilder()
                            .where(AudioDao.Properties.Id.eq(audio.getId()))
                            .unique();
                    if (existingAudio == null) {
                        File audioFile = MultimediaHelper.getFile(audio);
                        Log.i(getClass().getName(), "audioFile: " + audioFile);
                        if (!audioFile.exists()) {
                            // Download bytes
                            byte[] bytes = MultimediaLoader.loadMultimedia(EnvironmentSettings.getBaseUrl() + audioGson.getDownloadUrl());
                            Log.i(getClass().getName(), "bytes.length: " + bytes.length);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(audioFile);
                                IOUtils.write(bytes, fileOutputStream);
                                fileOutputStream.close();
                                Log.i(getClass().getName(), "Stored Audio file at " + audioFile.getAbsolutePath());
                            } catch (FileNotFoundException e) {
                                Log.e(getClass().getName(), null, e);
                            } catch (IOException e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                        if (audioFile.exists()) {
                            audioDao.insert(audio);
                            Log.i(getClass().getName(), "Stored Audio with id " + audio.getId() + " and transcription \"" + audio.getTranscription() + "\"");
                        }
                    } else {
                        Log.i(getClass().getName(), "Audio \"" + audio.getTranscription() + "\" already exists in database with id " + audio.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Images");
        url = EnvironmentSettings.getRestUrl() + "/content/multimedia/image/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("images");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Type type = new TypeToken<ImageGson>(){}.getType();
                    ImageGson imageGson = new Gson().fromJson(jsonArray.getString(i), type);
                    Image image = GsonToGreenDaoConverter.getImage(imageGson);
                    Image existingImage = imageDao.queryBuilder()
                            .where(ImageDao.Properties.Id.eq(image.getId()))
                            .unique();
                    if (existingImage == null) {
                        File imageFile = MultimediaHelper.getFile(image);
                        Log.i(getClass().getName(), "imageFile: " + imageFile);
                        if (!imageFile.exists()) {
                            // Download bytes
                            byte[] bytes = MultimediaLoader.loadMultimedia(EnvironmentSettings.getBaseUrl() + imageGson.getDownloadUrl());
                            Log.i(getClass().getName(), "bytes.length: " + bytes.length);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                                IOUtils.write(bytes, fileOutputStream);
                                fileOutputStream.close();
                                Log.i(getClass().getName(), "Stored Image file at " + imageFile.getAbsolutePath());
                            } catch (FileNotFoundException e) {
                                Log.e(getClass().getName(), null, e);
                            } catch (IOException e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                        if (imageFile.exists()) {
                            imageDao.insert(image);
                            Log.i(getClass().getName(), "Stored Image with id " + image.getId() + " and title \"" + image.getTitle() + "\"");
                        }
                    } else {
                        Log.i(getClass().getName(), "Image \"" + image.getTitle() + "\" already exists in database with id " + image.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Videos");
        url = EnvironmentSettings.getRestUrl() + "/content/multimedia/video/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
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
                        File videoFile = MultimediaHelper.getFile(video);
                        Log.i(getClass().getName(), "videoFile: " + videoFile);
                        if (!videoFile.exists()) {
                            // Download bytes
                            byte[] bytes = MultimediaLoader.loadMultimedia(EnvironmentSettings.getBaseUrl() + videoGson.getDownloadUrl());
                            Log.i(getClass().getName(), "bytes.length: " + bytes.length);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(videoFile);
                                IOUtils.write(bytes, fileOutputStream);
                                fileOutputStream.close();
                                Log.i(getClass().getName(), "Stored Video file at " + videoFile.getAbsolutePath());
                            } catch (FileNotFoundException e) {
                                Log.e(getClass().getName(), null, e);
                            } catch (IOException e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                        if (videoFile.exists()) {
                            videoDao.insert(video);
                            Log.i(getClass().getName(), "Stored Video with id " + video.getId() + " and title \"" + video.getTitle() + "\"");
                        }

                        File thumbnailFile = MultimediaHelper.getThumbnail(video);
                        Log.i(getClass().getName(), "thumbnailFile: " + thumbnailFile);
                        if (!thumbnailFile.exists()) {
                            // Download bytes
                            byte[] thumbnailBytes = MultimediaLoader.loadMultimedia(EnvironmentSettings.getBaseUrl() + videoGson.getThumbnailDownloadUrl());
                            Log.i(getClass().getName(), "thumbnailBytes.length: " + thumbnailBytes.length);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(thumbnailFile);
                                IOUtils.write(thumbnailBytes, fileOutputStream);
                                fileOutputStream.close();
                                Log.i(getClass().getName(), "Stored Video thumbnail at " + thumbnailFile.getAbsolutePath());
                            } catch (FileNotFoundException e) {
                                Log.e(getClass().getName(), null, e);
                            } catch (IOException e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                    } else {
                        Log.i(getClass().getName(), "Video \"" + video.getTitle() + "\" already exists in database with id " + video.getId());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        String result = "Content download complete. Please restart the application.";
        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.i(getClass().getName(), "onProgressUpdate");
        super.onProgressUpdate(values);

        String progressMessage = values[0];
        Log.i(getClass().getName(), "progressMessage: " + progressMessage);
        Toast.makeText(context, progressMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(getClass().getName(), "onPostExecute");
        super.onPostExecute(result);

        Log.i(getClass().getName(), "result: " + result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}