package org.literacyapp.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.CustomDaoMaster;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.contentprovider.dao.ImageDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.dao.VideoDao;
import org.literacyapp.contentprovider.dao.WordDao;
import org.literacyapp.contentprovider.model.Student;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.contentprovider.model.content.Word;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.contentprovider.model.content.multimedia.Image;
import org.literacyapp.contentprovider.model.content.multimedia.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing content downloaded from the website.
 */
public class ContentProvider {

    public static final int INITIAL_LETTER_COUNT = 3;

    public static final int INITIAL_NUMBER_COUNT = 3;

    private static DaoSession daoSession;

    public static DaoSession initializeDb(Context context) {
        Log.i(ContentProvider.class.getName(), "initializeDb");

        // Initialize greenDAO database
        String dbName = Environment.getExternalStorageDirectory() + "/.literacyapp/database/literacyapp-db";
        CustomDaoMaster.DevOpenHelper openHelper = new CustomDaoMaster.DevOpenHelper(context, dbName, null);
        SQLiteDatabase db = openHelper.getReadableDatabase(); // read-only
        CustomDaoMaster daoMaster = new CustomDaoMaster(db);
        daoSession = daoMaster.newSession();

        return daoSession;
    }

    public static List<Letter> getAvailableLetters() {
        Log.i(ContentProvider.class.getName(), "getAvailableLetters");

        // TODO: get current Student
        Student student = null;

        LetterDao letterDao = daoSession.getLetterDao();

        // Copied from CurriculumHelper.java:

        List<Letter> letters = new ArrayList<>();

        if (student == null) {
            // Student not yet identified

            // Fetch default list
            letters = letterDao.queryBuilder()
                    .orderDesc(LetterDao.Properties.UsageCount)
                    .orderAsc(LetterDao.Properties.Text)
                    .limit(INITIAL_LETTER_COUNT)
                    .list();

            // TODO: check for Letter learning events matching Device
        } else {
            // Fetch default list
            letters = letterDao.queryBuilder()
                    .orderDesc(LetterDao.Properties.UsageCount)
                    .orderAsc(LetterDao.Properties.Text)
                    .limit(INITIAL_LETTER_COUNT)
                    .list();

            // TODO: check for Letter learning events matching Student
        }

        return letters;
    }

    public static List<Number> getAvailableNumbers() {
        Log.i(ContentProvider.class.getName(), "getAvailableNumbers");

        // TODO: get current Student
        Student student = null;

        NumberDao numberDao = daoSession.getNumberDao();

        // Copied from CurriculumHelper.java:

        List<Number> numbers = new ArrayList<>();

        if (student == null) {
            // Student not yet identified

            // Fetch default list
            numbers = numberDao.queryBuilder()
                    .where(NumberDao.Properties.Value.notEq(0))
                    .orderAsc(NumberDao.Properties.Value)
                    .limit(INITIAL_NUMBER_COUNT)
                    .list();

            // TODO: check for Number learning events matching Device
        } else {
            // Fetch default list
            numbers = numberDao.queryBuilder()
                    .where(NumberDao.Properties.Value.notEq(0))
                    .orderAsc(NumberDao.Properties.Value)
                    .limit(INITIAL_NUMBER_COUNT)
                    .list();

            // TODO: check for Number learning events matching Student
        }

        return numbers;
    }

    public static List<Number> getAllNumbers() {
        Log.i(ContentProvider.class.getName(), "getAvailableNumbers");

        NumberDao numberDao = daoSession.getNumberDao();

        List<Number> numbers = numberDao.loadAll();

        return numbers;
    }

    public static List<Letter> getAllLetters() {
        Log.i(ContentProvider.class.getName(), "getAvailableLetters");

        LetterDao letterDao = daoSession.getLetterDao();

        List<Letter> letters = letterDao.loadAll();

        return letters;
    }

    public static List<Word> getAllWords() {
        Log.i(ContentProvider.class.getName(), "getAvailableWords");

        WordDao wordDao = daoSession.getWordDao();

        List<Word> words = wordDao.loadAll();

        return words;
    }

    public static List<Word> getAllWordsOrderedByFrequency() {
        Log.i(ContentProvider.class.getName(), "getAvailableWords");

        WordDao wordDao = daoSession.getWordDao();

        List<Word> words = wordDao.loadAll();

        return words;
    }

    // TODO: getAllStoryBooks()

    public static List<Audio> getAllAudios() {
        Log.i(ContentProvider.class.getName(), "getAvailableNumbers");

        AudioDao audioDao = daoSession.getAudioDao();

        List<Audio> audios = audioDao.loadAll();

        return audios;
    }
    
    public static List<Image> getAllImages() {
        Log.i(ContentProvider.class.getName(), "getAvailableNumbers");

        ImageDao imageDao = daoSession.getImageDao();
        
        List<Image> images = imageDao.loadAll();
        
        return images;
    }

//    public static List<Image> getImages(Word word) {
//        // TODO
//    }

    public static List<Video> getAllVideos() {
        Log.i(ContentProvider.class.getName(), "getAvailableNumbers");

        VideoDao videoDao = daoSession.getVideoDao();

        List<Video> videos = videoDao.loadAll();

        return videos;
    }
}
