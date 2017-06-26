package org.literacyapp.service.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.literacyapp.LiteracyApplication;
import org.literacyapp.contentprovider.dao.AllophoneDao;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.GsonToGreenDaoConverter;
import org.literacyapp.contentprovider.dao.ImageDao;
import org.literacyapp.contentprovider.dao.JoinAudiosWithLettersDao;
import org.literacyapp.contentprovider.dao.JoinAudiosWithNumbersDao;
import org.literacyapp.contentprovider.dao.JoinAudiosWithWordsDao;
import org.literacyapp.contentprovider.dao.JoinImagesWithLettersDao;
import org.literacyapp.contentprovider.dao.JoinImagesWithNumbersDao;
import org.literacyapp.contentprovider.dao.JoinImagesWithWordsDao;
import org.literacyapp.contentprovider.dao.JoinLettersWithAllophonesDao;
import org.literacyapp.contentprovider.dao.JoinVideosWithLettersDao;
import org.literacyapp.contentprovider.dao.JoinVideosWithNumbersDao;
import org.literacyapp.contentprovider.dao.JoinVideosWithWordsDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.dao.StoryBookDao;
import org.literacyapp.contentprovider.dao.VideoDao;
import org.literacyapp.contentprovider.dao.WordDao;
import org.literacyapp.contentprovider.model.JoinLettersWithAllophones;
import org.literacyapp.contentprovider.model.content.Allophone;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.contentprovider.model.content.StoryBook;
import org.literacyapp.contentprovider.model.content.Word;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.contentprovider.model.content.multimedia.Image;
import org.literacyapp.contentprovider.model.content.multimedia.JoinAudiosWithLetters;
import org.literacyapp.contentprovider.model.content.multimedia.JoinAudiosWithNumbers;
import org.literacyapp.contentprovider.model.content.multimedia.JoinAudiosWithWords;
import org.literacyapp.contentprovider.model.content.multimedia.JoinImagesWithLetters;
import org.literacyapp.contentprovider.model.content.multimedia.JoinImagesWithNumbers;
import org.literacyapp.contentprovider.model.content.multimedia.JoinImagesWithWords;
import org.literacyapp.contentprovider.model.content.multimedia.JoinVideosWithLetters;
import org.literacyapp.contentprovider.model.content.multimedia.JoinVideosWithNumbers;
import org.literacyapp.contentprovider.model.content.multimedia.JoinVideosWithWords;
import org.literacyapp.contentprovider.model.content.multimedia.Video;
import org.literacyapp.model.gson.content.AllophoneGson;
import org.literacyapp.model.gson.content.LetterGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.StoryBookGson;
import org.literacyapp.model.gson.content.SyllableGson;
import org.literacyapp.model.gson.content.WordGson;
import org.literacyapp.model.gson.content.multimedia.AudioGson;
import org.literacyapp.model.gson.content.multimedia.ImageGson;
import org.literacyapp.model.gson.content.multimedia.VideoGson;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.EnvironmentSettings;
import org.literacyapp.util.JsonLoader;
import org.literacyapp.contentprovider.util.MultimediaHelper;
import org.literacyapp.util.MultimediaDownloader;

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
    private StoryBookDao storyBookDao;
    private AudioDao audioDao;
    private ImageDao imageDao;
    private VideoDao videoDao;

    private JoinLettersWithAllophonesDao joinLettersWithAllophonesDao;
    
    private JoinVideosWithLettersDao joinVideosWithLettersDao;
    private JoinVideosWithNumbersDao joinVideosWithNumbersDao;
    private JoinVideosWithWordsDao joinVideosWithWordsDao;

    private JoinAudiosWithLettersDao joinAudiosWithLettersDao;
    private JoinAudiosWithNumbersDao joinAudiosWithNumbersDao;
    private JoinAudiosWithWordsDao joinAudiosWithWordsDao;

    private JoinImagesWithLettersDao joinImagesWithLettersDao;
    private JoinImagesWithNumbersDao joinImagesWithNumbersDao;
    private JoinImagesWithWordsDao joinImagesWithWordsDao;

    public DownloadContentAsyncTask(Context context) {
        this.context = context;

        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        allophoneDao = literacyApplication.getDaoSession().getAllophoneDao();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        wordDao = literacyApplication.getDaoSession().getWordDao();
        storyBookDao = literacyApplication.getDaoSession().getStoryBookDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
        imageDao = literacyApplication.getDaoSession().getImageDao();
        videoDao = literacyApplication.getDaoSession().getVideoDao();

        joinLettersWithAllophonesDao = literacyApplication.getDaoSession().getJoinLettersWithAllophonesDao();
        
        joinVideosWithLettersDao = literacyApplication.getDaoSession().getJoinVideosWithLettersDao();
        joinVideosWithNumbersDao = literacyApplication.getDaoSession().getJoinVideosWithNumbersDao();
        joinVideosWithWordsDao = literacyApplication.getDaoSession().getJoinVideosWithWordsDao();

        joinAudiosWithLettersDao = literacyApplication.getDaoSession().getJoinAudiosWithLettersDao();
        joinAudiosWithNumbersDao = literacyApplication.getDaoSession().getJoinAudiosWithNumbersDao();
        joinAudiosWithWordsDao = literacyApplication.getDaoSession().getJoinAudiosWithWordsDao();

        joinImagesWithLettersDao = literacyApplication.getDaoSession().getJoinImagesWithLettersDao();
        joinImagesWithNumbersDao = literacyApplication.getDaoSession().getJoinImagesWithNumbersDao();
        joinImagesWithWordsDao = literacyApplication.getDaoSession().getJoinImagesWithWordsDao();
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
                        Log.i(getClass().getName(), "Storing Allophone, id: " + allophone.getId() + ", valueIpa: /" + allophone.getValueIpa() + "/, valueSampa: \"" + allophone.getValueSampa() + "\", revisionNumber: " + allophone.getRevisionNumber());
                        allophoneDao.insert(allophone);
                    } else if (existingAllophone.getRevisionNumber() < allophone.getRevisionNumber()) {
                        Log.i(getClass().getName(), "Updating Allophone with id " + existingAllophone.getId() + " from revisionNumber " + existingAllophone.getRevisionNumber() + " to revisionNumber " + allophone.getRevisionNumber());
                        allophoneDao.update(allophone);
                    } else {
                        Log.i(getClass().getName(), "Allophone /" + allophone.getValueIpa() + "/ already exists in database with id " + allophone.getId() + " (revision " + allophone.getRevisionNumber() + ")");
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
                        Log.i(getClass().getName(), "Storing Letter, id: " + letter.getId() + ", text: \"" + letter.getText() + "\", revisionNumber: " + letter.getRevisionNumber());
                        letterDao.insert(letter);

                        for (AllophoneGson allophoneGson : letterGson.getAllophones()) {
                            Allophone allophone = allophoneDao.queryBuilder()
                                    .where(
                                            AllophoneDao.Properties.Locale.eq(allophoneGson.getLocale()),
                                            AllophoneDao.Properties.ValueSampa.eq(allophoneGson.getValueSampa())
                                    )
                                    .unique();
                            Log.i(getClass().getName(), "Storing Allophone, id: " + allophone.getId() + ", valueIpa: /" + allophone.getValueIpa() + "/, valueSampa: \"" + allophone.getValueSampa() + "\", for letter '" + letter.getText() + "'");

                            JoinLettersWithAllophones joinLettersWithAllophones = new JoinLettersWithAllophones();
                            joinLettersWithAllophones.setLetterId(letter.getId());
                            joinLettersWithAllophones.setAllophoneId(allophone.getId());
                            joinLettersWithAllophonesDao.insert(joinLettersWithAllophones);
                        }
                    } else if (existingLetter.getRevisionNumber() < letter.getRevisionNumber()) {
                        Log.i(getClass().getName(), "Updating Letter with id " + existingLetter.getId() + " from revisionNumber " + existingLetter.getRevisionNumber() + " to revisionNumber " + letter.getRevisionNumber());
                        letterDao.update(letter);

                        // TODO: update JoinLettersWithAllophones
                    } else {
                        Log.i(getClass().getName(), "Letter \"" + letter.getText() + "\" already exists in database with id " + letter.getId() + " (revision " + letter.getRevisionNumber() + ")");
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
                        Log.i(getClass().getName(), "Storing Number, id: " + number.getId() + ", value: \"" + number.getValue() + "\", revisionNumber: " + number.getRevisionNumber());
                        numberDao.insert(number);
                    } else if (existingNumber.getRevisionNumber() < number.getRevisionNumber()) {
                        Log.i(getClass().getName(), "Updating Number with id " + existingNumber.getId() + " from revisionNumber " + existingNumber.getRevisionNumber() + " to revisionNumber " + number.getRevisionNumber());
                        numberDao.update(number);
                    } else {
                        Log.i(getClass().getName(), "Number \"" + number.getValue() + "\" already exists in database with id " + number.getId() + " (revision " + number.getRevisionNumber() + ")");
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading Syllables");
        url = EnvironmentSettings.getRestUrl() + "/content/syllable/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("syllables");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Type type = new TypeToken<SyllableGson>(){}.getType();
                    SyllableGson syllableGson = new Gson().fromJson(jsonArray.getString(i), type);
//                    Syllable syllable = GsonToGreenDaoConverter.getSyllable(syllableGson);
//                    Syllable existingSyllable = syllableDao.queryBuilder()
//                            .where(SyllableDao.Properties.Id.eq(syllable.getId()))
//                            .unique();
//                    if (existingSyllable == null) {
//                        Log.i(getClass().getName(), "Storing Syllable, id: " + syllable.getId() + ", text: \"" + syllable.getText() + "\", revisionNumber: " + syllable.getRevisionNumber());
//                        syllableDao.insert(syllable);
//                    } else if (existingSyllable.getRevisionNumber() < syllable.getRevisionNumber()) {
//                        Log.i(getClass().getName(), "Updating Syllable with id " + existingSyllable.getId() + " from revisionNumber " + existingSyllable.getRevisionNumber() + " to revisionNumber " + syllable.getRevisionNumber());
//                        syllableDao.update(syllable);
//                    } else {
//                        Log.i(getClass().getName(), "Syllable \"" + syllable.getText() + "\" already exists in database with id " + syllable.getId() + " (revision " + syllable.getRevisionNumber() + ")");
//                    }
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
                        Log.i(getClass().getName(), "Storing Word, id: " + word.getId() + ", text: \"" + word.getText() + "\", revisionNumber: " + word.getRevisionNumber());
                        wordDao.insert(word);
                    } else if (existingWord.getRevisionNumber() < word.getRevisionNumber()) {
                        Log.i(getClass().getName(), "Updating Word with id " + existingWord.getId() + " from revisionNumber " + existingWord.getRevisionNumber() + " to revisionNumber " + word.getRevisionNumber());
                        wordDao.update(word);
                    } else {
                        Log.i(getClass().getName(), "Word \"" + word.getText() + "\" already exists in database with id " + word.getId() + " (revision " + word.getRevisionNumber() + ")");
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), null, e);
        }


        publishProgress("Downloading StoryBooks");
        url = EnvironmentSettings.getRestUrl() + "/content/storybook/list" +
                "?deviceId=" + DeviceInfoHelper.getDeviceId(context) +
                "&locale=" + DeviceInfoHelper.getLocale(context);
        jsonResponse = JsonLoader.loadJson(url);
        Log.i(getClass().getName(), "jsonResponse: " + jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!"success".equals(jsonObject.getString("result"))) {
                Log.w(getClass().getName(), "Download failed");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("storyBooks");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Type type = new TypeToken<StoryBookGson>(){}.getType();
                    StoryBookGson storyBookGson = new Gson().fromJson(jsonArray.getString(i), type);
                    StoryBook storyBook = GsonToGreenDaoConverter.getStoryBook(storyBookGson);
                    StoryBook existingStoryBook = storyBookDao.queryBuilder()
                            .where(StoryBookDao.Properties.Id.eq(storyBook.getId()))
                            .unique();
                    if (existingStoryBook == null) {
                        Log.i(getClass().getName(), "Storing StoryBook, id: " + storyBook.getId() + ", title: \"" + storyBook.getTitle() + "\", revisionNumber: " + storyBook.getRevisionNumber());
                        storyBookDao.insert(storyBook);
                    } else if (existingStoryBook.getRevisionNumber() < storyBook.getRevisionNumber()) {
                        Log.i(getClass().getName(), "Updating StoryBook with id " + existingStoryBook.getId() + " from revisionNumber " + existingStoryBook.getRevisionNumber() + " to revisionNumber " + storyBook.getRevisionNumber());
                        storyBookDao.update(storyBook);
                    } else {
                        Log.i(getClass().getName(), "StoryBook \"" + storyBook.getTitle() + "\" already exists in database with id " + storyBook.getId() + " (revision " + storyBook.getRevisionNumber() + ")");
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

                    File audioFile = MultimediaHelper.getFile(audio);
                    Log.i(getClass().getName(), "audioFile: " + audioFile);
                    if (!audioFile.exists()) {
                        // Download bytes
                        byte[] bytes = MultimediaDownloader.downloadMultimedia(EnvironmentSettings.getBaseUrl() + audioGson.getDownloadUrl());
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
                        Audio existingAudio = audioDao.queryBuilder()
                                .where(AudioDao.Properties.Id.eq(audio.getId()))
                                .unique();
                        if (existingAudio == null) {
                            Log.i(getClass().getName(), "Storing Audio. id: " + audio.getId() + ", transcription: \"" + audio.getTranscription() + "\", revisionNumber: " + audio.getRevisionNumber());
                            audioDao.insert(audio);

                            // Store Letters
                            if (audioGson.getLetters() != null) {
                                for (LetterGson letterGson : audioGson.getLetters()) {
                                    JoinAudiosWithLetters joinAudiosWithLetters = new JoinAudiosWithLetters();
                                    joinAudiosWithLetters.setAudioId(audio.getId());
                                    joinAudiosWithLetters.setLetterId(letterGson.getId());
                                    joinAudiosWithLettersDao.insert(joinAudiosWithLetters);
                                }
                            }

                            // Store Numbers
                            if (audioGson.getNumbers() != null) {
                                for (NumberGson numberGson : audioGson.getNumbers()) {
                                    JoinAudiosWithNumbers joinAudiosWithNumbers = new JoinAudiosWithNumbers();
                                    joinAudiosWithNumbers.setAudioId(audio.getId());
                                    joinAudiosWithNumbers.setNumberId(numberGson.getId());
                                    joinAudiosWithNumbersDao.insert(joinAudiosWithNumbers);
                                }
                            }

                            // Store Words
                            if (audioGson.getWords() != null) {
                                for (WordGson wordGson : audioGson.getWords()) {
                                    JoinAudiosWithWords joinAudiosWithWords = new JoinAudiosWithWords();
                                    joinAudiosWithWords.setAudioId(audio.getId());
                                    joinAudiosWithWords.setWordId(wordGson.getId());
                                    joinAudiosWithWordsDao.insert(joinAudiosWithWords);
                                }
                            }
                        } else if (existingAudio.getRevisionNumber() < audio.getRevisionNumber()) {
                            Log.i(getClass().getName(), "Updating Audio with id " + existingAudio.getId() + " from revisionNumber " + existingAudio.getRevisionNumber() + " to revisionNumber " + audio.getRevisionNumber());
                            audioDao.update(audio);
                        } else {
                            Log.i(getClass().getName(), "Audio \"" + audio.getTranscription() + "\" already exists in database with id " + audio.getId() + " (revision " + audio.getRevisionNumber() + ")");
                        }
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

                    File imageFile = MultimediaHelper.getFile(image);
                    Log.i(getClass().getName(), "imageFile: " + imageFile);
                    if (!imageFile.exists()) {
                        // Download bytes
                        byte[] bytes = MultimediaDownloader.downloadMultimedia(EnvironmentSettings.getBaseUrl() + imageGson.getDownloadUrl());
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
                        Image existingImage = imageDao.queryBuilder()
                                .where(ImageDao.Properties.Id.eq(image.getId()))
                                .unique();
                        if (existingImage == null) {
                            Log.i(getClass().getName(), "Storing Image. id: " + image.getId() + ", title: \"" + image.getTitle() + "\", revisionNumber: " + image.getRevisionNumber());
                            imageDao.insert(image);

                            // Store Letters
                            if (imageGson.getLetters() != null) {
                                for (LetterGson letterGson : imageGson.getLetters()) {
                                    JoinImagesWithLetters joinImagesWithLetters = new JoinImagesWithLetters();
                                    joinImagesWithLetters.setImageId(image.getId());
                                    joinImagesWithLetters.setLetterId(letterGson.getId());
                                    joinImagesWithLettersDao.insert(joinImagesWithLetters);
                                }
                            }

                            // Store Numbers
                            if (imageGson.getNumbers() != null) {
                                for (NumberGson numberGson : imageGson.getNumbers()) {
                                    JoinImagesWithNumbers joinImagesWithNumbers = new JoinImagesWithNumbers();
                                    joinImagesWithNumbers.setImageId(image.getId());
                                    joinImagesWithNumbers.setNumberId(numberGson.getId());
                                    joinImagesWithNumbersDao.insert(joinImagesWithNumbers);
                                }
                            }

                            // Store Words
                            if (imageGson.getWords() != null) {
                                for (WordGson wordGson : imageGson.getWords()) {
                                    JoinImagesWithWords joinImagesWithWords = new JoinImagesWithWords();
                                    joinImagesWithWords.setImageId(image.getId());
                                    joinImagesWithWords.setWordId(wordGson.getId());
                                    joinImagesWithWordsDao.insert(joinImagesWithWords);
                                }
                            }
                        } else if (existingImage.getRevisionNumber() < image.getRevisionNumber()) {
                            Log.i(getClass().getName(), "Updating Image with id " + existingImage.getId() + " from revisionNumber " + existingImage.getRevisionNumber() + " to revisionNumber " + image.getRevisionNumber());
                            imageDao.update(image);
                        } else {
                            Log.i(getClass().getName(), "Image \"" + image.getTitle() + "\" already exists in database with id " + image.getId() + " (revision " + image.getRevisionNumber() + ")");
                        }
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

                    File videoFile = MultimediaHelper.getFile(video);
                    Log.i(getClass().getName(), "videoFile: " + videoFile);
                    if (!videoFile.exists()) {
                        // Download bytes
                        byte[] bytes = MultimediaDownloader.downloadMultimedia(EnvironmentSettings.getBaseUrl() + videoGson.getDownloadUrl());
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

                    File thumbnailFile = MultimediaHelper.getVideoThumbnail(video);
                    Log.i(getClass().getName(), "thumbnailFile: " + thumbnailFile);
                    if (!thumbnailFile.exists()) {
                        // Download bytes
                        byte[] thumbnailBytes = MultimediaDownloader.downloadMultimedia(EnvironmentSettings.getBaseUrl() + videoGson.getThumbnailDownloadUrl());
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
                    
                    if (videoFile.exists() && thumbnailFile.exists()) {
                        Video existingVideo = videoDao.queryBuilder()
                                .where(VideoDao.Properties.Id.eq(video.getId()))
                                .unique();
                        if (existingVideo == null) {
                            Log.i(getClass().getName(), "Storing Video. id: " + video.getId() + ", title: \"" + video.getTitle() + "\", revisionNumber: " + video.getRevisionNumber());
                            videoDao.insert(video);

                            // Store Letters
                            if (videoGson.getLetters() != null) {
                                for (LetterGson letterGson : videoGson.getLetters()) {
                                    JoinVideosWithLetters joinVideosWithLetters = new JoinVideosWithLetters();
                                    joinVideosWithLetters.setVideoId(video.getId());
                                    joinVideosWithLetters.setLetterId(letterGson.getId());
                                    joinVideosWithLettersDao.insert(joinVideosWithLetters);
                                }
                            }

                            // Store Numbers
                            if (videoGson.getNumbers() != null) {
                                for (NumberGson numberGson : videoGson.getNumbers()) {
                                    JoinVideosWithNumbers joinVideosWithNumbers = new JoinVideosWithNumbers();
                                    joinVideosWithNumbers.setVideoId(video.getId());
                                    joinVideosWithNumbers.setNumberId(numberGson.getId());
                                    joinVideosWithNumbersDao.insert(joinVideosWithNumbers);
                                }
                            }

                            // Store Words
                            if (videoGson.getWords() != null) {
                                for (WordGson wordGson : videoGson.getWords()) {
                                    JoinVideosWithWords joinVideosWithWords = new JoinVideosWithWords();
                                    joinVideosWithWords.setVideoId(video.getId());
                                    joinVideosWithWords.setWordId(wordGson.getId());
                                    joinVideosWithWordsDao.insert(joinVideosWithWords);
                                }
                            }
                        } else if (existingVideo.getRevisionNumber() < video.getRevisionNumber()) {
                            Log.i(getClass().getName(), "Updating Video with id " + existingVideo.getId() + " from revisionNumber " + existingVideo.getRevisionNumber() + " to revisionNumber " + video.getRevisionNumber());
                            videoDao.update(video);
                        } else {
                            Log.i(getClass().getName(), "Video \"" + video.getTitle() + "\" already exists in database with id " + video.getId() + " (revision " + video.getRevisionNumber() + ")");
                        }
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
//        Toast.makeText(context, progressMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(getClass().getName(), "onPostExecute");
        super.onPostExecute(result);

        Log.i(getClass().getName(), "result: " + result);
//        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}