package org.literacyapp.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.literacyapp.contentprovider.dao.AllophoneDao;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.DaoMaster;
import org.literacyapp.contentprovider.dao.DaoSession;
import org.literacyapp.contentprovider.dao.ImageDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.dao.StoryBookDao;
import org.literacyapp.contentprovider.dao.VideoDao;
import org.literacyapp.contentprovider.dao.WordDao;
import org.literacyapp.contentprovider.model.Student;
import org.literacyapp.contentprovider.model.content.Allophone;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.contentprovider.model.content.StoryBook;
import org.literacyapp.contentprovider.model.content.Word;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.contentprovider.model.content.multimedia.Image;
import org.literacyapp.contentprovider.model.content.multimedia.Video;
import org.literacyapp.model.enums.GradeLevel;
import org.literacyapp.model.enums.content.SpellingConsistency;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing content downloaded from the website.
 */
public class ContentProvider {

    private static final int INITIAL_LETTER_COUNT = 3;

    private static final int INITIAL_NUMBER_COUNT = 3;

    private static DaoSession daoSession;

    public static DaoSession initializeDb(Context context) {
        Log.i(ContentProvider.class.getName(), "initializeDb");

        // Initialize greenDAO database
        String dbName = Environment.getExternalStorageDirectory() + "/.literacyapp/database/literacyapp-db";
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, dbName);
        SQLiteDatabase db = openHelper.getReadableDatabase(); // read-only
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        return daoSession;
    }

    /**
     * Only use this method if none of the other methods return what you need, and you want to
     * create a custom query.
     */
    public static DaoSession getDaoSession() {
        return daoSession;
    }

    // TODO: getUnlockedAllophones

    public static List<Allophone> getAllAllophones() {
        Log.i(ContentProvider.class.getName(), "getAllAllophones");

        AllophoneDao allophoneDao = daoSession.getAllophoneDao();

        List<Allophone> allophones = allophoneDao.queryBuilder()
                .orderDesc(AllophoneDao.Properties.UsageCount)
                .orderAsc(AllophoneDao.Properties.ValueIpa)
                .list();

        return allophones;
    }

    /**
     * Returns a list of all letters available to the current student.
     */
    public static List<Letter> getUnlockedLetters() {
        Log.i(ContentProvider.class.getName(), "getUnlockedLetters");

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

    /**
     * Returns a list of all letters, including those not yet made available to the current student.
     */
    public static List<Letter> getAllLetters() {
        Log.i(ContentProvider.class.getName(), "getAllLetters");

        LetterDao letterDao = daoSession.getLetterDao();

        List<Letter> letters = letterDao.queryBuilder()
                .orderDesc(LetterDao.Properties.UsageCount)
                .orderAsc(LetterDao.Properties.Text)
                .list();

        return letters;
    }

    /**
     * Returns a list of all numbers available to the current student.
     */
    public static List<Number> getUnlockedNumbers() {
        Log.i(ContentProvider.class.getName(), "getUnlockedNumbers");

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

    /**
     * Returns a list of all numbers, including those not yet made available to the current student.
     */
    public static List<Number> getAllNumbers() {
        Log.i(ContentProvider.class.getName(), "getAllNumbers");

        NumberDao numberDao = daoSession.getNumberDao();

        List<Number> numbers = numberDao.loadAll();
        // TODO: order by value?

        return numbers;
    }

//    /**
//     * Returns a list of all words available to the current student
//     */
//    public static List<Word> getUnlockedWords(SpellingConsistency... spellingConsistencies) {
//        Log.i(ContentProvider.class.getName(), "getUnlockedWords");
//
//        WordDao wordDao = daoSession.getWordDao();
//
//        // TODO
//
//        return words;
//    }

    /**
     * Returns a list of all words, including those not yet made available to the current student.
     */
    public static List<Word> getAllWords(SpellingConsistency... spellingConsistencies) {
        Log.i(ContentProvider.class.getName(), "getAllWords");

        WordDao wordDao = daoSession.getWordDao();

        List<Word> words = wordDao.queryBuilder()
                .where(WordDao.Properties.SpellingConsistency.in(spellingConsistencies))
                .orderDesc(WordDao.Properties.UsageCount)
                .orderAsc(WordDao.Properties.Text)
                .list();

        return words;
    }

    /**
     * Returns a list of all StoryBooks available to the current student.
     */
    // TODO: getUnlockedStoryBooks()

    /**
     * Returns a list of all StoryBooks, including those not yet made available to the current student.
     */
    public static List<StoryBook> getAllStoryBooks(GradeLevel... gradeLevels) {
        Log.i(ContentProvider.class.getName(), "getAllStoryBooks");

        StoryBookDao storyBookDao = daoSession.getStoryBookDao();

        List<StoryBook> storyBooks = storyBookDao.queryBuilder()
                .where(StoryBookDao.Properties.GradeLevel.in(gradeLevels))
                .orderAsc(StoryBookDao.Properties.Title)
                .list();

        return storyBooks;
    }

    public static List<Audio> getAllAudios() {
        Log.i(ContentProvider.class.getName(), "getAllAudios");

        AudioDao audioDao = daoSession.getAudioDao();

        List<Audio> audios = audioDao.queryBuilder()
                .orderAsc(AudioDao.Properties.Transcription)
                .list();

        return audios;
    }

    public static Audio getAudio(String title) {
        Log.i(ContentProvider.class.getName(), "getAudio");

        AudioDao audioDao = daoSession.getAudioDao();

        Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq(title))
                .unique();

        return audio;
    }
    
    public static List<Image> getAllImages() {
        Log.i(ContentProvider.class.getName(), "getAllImages");

        ImageDao imageDao = daoSession.getImageDao();
        
        List<Image> images = imageDao.queryBuilder()
                .orderAsc(ImageDao.Properties.Title)
                .list();
        
        return images;
    }

    public static Image getImage(String title) {
        Log.i(ContentProvider.class.getName(), "getImage");

        ImageDao imageDao = daoSession.getImageDao();

        Image image = imageDao.queryBuilder()
                .where(ImageDao.Properties.Title.eq(title))
                .unique();

        return image;
    }

    public static List<Video> getAllVideos() {
        Log.i(ContentProvider.class.getName(), "getAllVideos");

        VideoDao videoDao = daoSession.getVideoDao();

        List<Video> videos = videoDao.queryBuilder()
                .orderAsc(VideoDao.Properties.Title)
                .list();

        return videos;
    }
}
