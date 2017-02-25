package org.literacyapp.logic;

import android.content.Context;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.model.Student;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.model.enums.content.NumeracySkill;

import java.util.ArrayList;
import java.util.List;

public class CurriculumHelper {

    public static final int INITIAL_LETTER_COUNT = 3;

    public static final int INITIAL_NUMBER_COUNT = 3;

    private LetterDao letterDao;

    private NumberDao numberDao;

    public CurriculumHelper(Context context) {
        Log.i(getClass().getName(), "CurriculumHelper");

        LiteracyApplication literacyApplication = (LiteracyApplication) context;
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
    }

    /**
     * If the Student has not yet been identified, use {@code null} as parameter to fall back to
     * default list.
     */
    public List<Letter> getAvailableLetters(Student student) {
        Log.i(getClass().getName(), "getUnlockedLetters");

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
     * If the Student has not yet been identified, use {@code null} as parameter to fall back to
     * default list.
     */
    public List<Number> getAvailableNumbers(Student student) {
        Log.i(getClass().getName(), "getUnlockedNumbers");

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

//    TODO: getAvailableWords

    /**
     * If the Student has not yet been identified, use {@code null} as parameter to fall back to
     * default list.
     */
    public List<LiteracySkill> getAvailableLiteracySkills(Student student) {
        Log.i(getClass().getName(), "getAvailableLiteracySkills");

        List<LiteracySkill> literacySkills = new ArrayList<>();

        if (student == null) {
            // Student not yet identified

            // Fall back to default list
            literacySkills.add(LiteracySkill.ORAL_VOCABULARY);
            literacySkills.add(LiteracySkill.LISTENING_COMPREHENSION);
            literacySkills.add(LiteracySkill.CONCEPTS_ABOUT_PRINT);
            literacySkills.add(LiteracySkill.PHONEMIC_AWARENESS);
            literacySkills.add(LiteracySkill.LETTER_IDENTIFICATION);

            // TODO: check for LiteracySkill learning events matching Device
        } else {
            // Fall back to default list
            literacySkills.add(LiteracySkill.ORAL_VOCABULARY);
            literacySkills.add(LiteracySkill.LISTENING_COMPREHENSION);
            literacySkills.add(LiteracySkill.CONCEPTS_ABOUT_PRINT);
            literacySkills.add(LiteracySkill.PHONEMIC_AWARENESS);
            literacySkills.add(LiteracySkill.LETTER_IDENTIFICATION);

            // TODO: check for LiteracySkill learning events matching Student
        }

        return literacySkills;
    }

    /**
     * If the Student has not yet been identified, use {@code null} as parameter to fall back to
     * default list.
     */
    public List<NumeracySkill> getAvailableNumeracySkills(Student student) {
        Log.i(getClass().getName(), "getAvailableNumeracySkills");

        List<NumeracySkill> numeracySkills = new ArrayList<>();

        if (student == null) {
            // Student not yet identified

            // Fall back to default list
            numeracySkills.add(NumeracySkill.SHAPE_IDENTIFICATION);
            numeracySkills.add(NumeracySkill.SHAPE_NAMING);
            numeracySkills.add(NumeracySkill.ORAL_COUNTING);
            numeracySkills.add(NumeracySkill.ONE_TO_ONE_CORRESPONDENCE);
            numeracySkills.add(NumeracySkill.NUMBER_IDENTIFICATION);

            // TODO: check for NumeracySkill learning events matching Device
        } else {
            // Fall back to default list
            numeracySkills.add(NumeracySkill.SHAPE_IDENTIFICATION);
            numeracySkills.add(NumeracySkill.SHAPE_NAMING);
            numeracySkills.add(NumeracySkill.ORAL_COUNTING);
            numeracySkills.add(NumeracySkill.ONE_TO_ONE_CORRESPONDENCE);
            numeracySkills.add(NumeracySkill.NUMBER_IDENTIFICATION);

            // TODO: check for NumeracySkill learning events matching Student
        }

        return numeracySkills;
    }
}

