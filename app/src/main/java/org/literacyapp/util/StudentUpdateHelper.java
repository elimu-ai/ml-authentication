package org.literacyapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.Student;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.model.enums.content.NumeracySkill;

import java.util.ArrayList;
import java.util.List;

/**
 * Personalize apps/content according to Student's level.
 * <p />
 * This class will be used in 2 different scenarios:
 *    1. During Student level change (same Student as before, but with updated literacy/numeracy skills).
 *    2. During Student change (another Student than before).
 */
public class StudentUpdateHelper {

    private Context context;

    private Student student;

    public StudentUpdateHelper(Context context, Student student) {
        this.context = context;
        this.student = student;
    }

    public void updateStudent() {
        Log.i(getClass().getName(), "updateStudent");

        if (student == null) {
            Log.i(getClass().getName(), "Updating Student. No specific Student id provided.");
        } else {
            Log.i(getClass().getName(), "Updating Student: " + student.getUniqueId());
        }

        notifyUiApplication();

        // TODO: fetch currently active Student (if any)
        // TODO: if not same as previous Student, store StudentChangedEvent

        // TODO: if Student's level changed, store LevelCompletedEvent
    }

    /**
     * Notify the UI application about the current level of the updated Student.
     * <p />
     * https://github.com/literacyapp-org/literacyapp-ui
     */
    private void notifyUiApplication() {
        Log.i(getClass().getName(), "Notifying external applications about updated content via broadcast");
        CurriculumHelper curriculumHelper = new CurriculumHelper(context);

        List<Letter> availableLetters = curriculumHelper.getAvailableLetters(student);
        ArrayList<String> availableLettersStringArrayList = new ArrayList<>();
        for (Letter letter : availableLetters) {
            availableLettersStringArrayList.add(letter.getText());
        }
        Log.i(getClass().getName(), "availableLettersStringArrayList: " + availableLettersStringArrayList);

        List<Number> availableNumbers = curriculumHelper.getAvailableNumbers(student);
        ArrayList<String> availableNumbersStringArrayList = new ArrayList<>();
        for (Number number : availableNumbers) {
            availableNumbersStringArrayList.add(number.getValue().toString());
        }
        Log.i(getClass().getName(), "availableNumbersStringArrayList: " + availableNumbersStringArrayList);

        List<LiteracySkill> availableLiteracySkills = curriculumHelper.getAvailableLiteracySkills(student);
        ArrayList<String> availableLiteracySkillsStringArrayList = new ArrayList<>();
        for (LiteracySkill literacySkill : availableLiteracySkills) {
            availableLiteracySkillsStringArrayList.add(literacySkill.toString());
        }
        Log.i(getClass().getName(), "availableLiteracySkillsStringArrayList: " + availableLiteracySkillsStringArrayList);

        List<NumeracySkill> availableNumeracySkills = curriculumHelper.getAvailableNumeracySkills(student);
        ArrayList<String> availableNumeracySkillsStringArrayList = new ArrayList<>();
        for (NumeracySkill numeracySkill : availableNumeracySkills) {
            availableNumeracySkillsStringArrayList.add(numeracySkill.toString());
        }
        Log.i(getClass().getName(), "availableNumeracySkillsStringArrayList: " + availableNumeracySkillsStringArrayList);

        Intent intent = new Intent();
        intent.setPackage("org.literacyapp.ui");
        intent.setAction("literacyapp.intent.action.STUDENT_UPDATED");
        intent.putExtra("packageName", context.getPackageName());
        intent.putStringArrayListExtra("availableLetters", availableLettersStringArrayList);
        intent.putStringArrayListExtra("availableNumbers", availableNumbersStringArrayList);
        intent.putStringArrayListExtra("availableLiteracySkills", availableLiteracySkillsStringArrayList);
        intent.putStringArrayListExtra("availableNumeracySkills", availableNumeracySkillsStringArrayList);
        if ((student != null) && (student.getUniqueId() != null)) {
            intent.putExtra("studentId", student.getUniqueId());
        }
        if ((student != null) && (student.getAvatar() != null)) {
            intent.putExtra("studentAvatar", student.getAvatar().getImageFileUrl());
        }
        Log.i(getClass().getName(), "Sending broadcast to " + intent.getPackage());
        context.sendBroadcast(intent);
    }
}
