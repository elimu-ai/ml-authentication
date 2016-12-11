package org.literacyapp.authentication.fallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.StudentImage;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.StudentHelper;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * This activity should be triggered after a determined period of device inactivity.
 * <p />
 * If no Students have been stored on the device, redirect to {@link StudentRegistrationActivity}.
 * <p />
 * If Students have been stored on device, redirect to {@link StudentSelectionActivity}
 */
public class StudentAuthenticationActivity extends AppCompatActivity {

    private StudentDao studentDao;

    private StudentImageDao studentImageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authentication);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplication();
        studentDao = literacyApplication.getDaoSession().getStudentDao();
        studentImageDao = literacyApplication.getDaoSession().getStudentImageDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        List<Student> existingStudents = studentDao.loadAll();
        Log.i(getClass().getName(), "existingStudents.size(): " + existingStudents.size());

        if (existingStudents.isEmpty()) {
            // Fetch test Students from SD card
            File testStudentsDirectory = StudentHelper.getTestStudentsDirectory();
            Log.i(getClass().getName(), "Looking up test students from " + testStudentsDirectory);
            File[] testStudentImageFiles = testStudentsDirectory.listFiles();
            Log.i(getClass().getName(), "testStudentImageFiles.length: " + testStudentImageFiles.length);
            for (int i = 0; i < testStudentImageFiles.length; i++) {
                File testStudentImageFile = testStudentImageFiles[i];
                Log.i(getClass().getName(), "Storing test student in database: " + testStudentImageFile);
                String fileName = testStudentImageFile.getName();
                Log.i(getClass().getName(), "fileName: " + fileName);

                Log.i(getClass().getName(), "Storing test StudentImage in database");
                StudentImage studentImage = new StudentImage();
                studentImage.setImageFileUrl(testStudentImageFile.getAbsolutePath());
                studentImage.setTimeCollected(Calendar.getInstance());
                studentImageDao.insert(studentImage);
                Log.i(getClass().getName(), "StudentImage stored in database with id " + studentImage.getId());

                Log.i(getClass().getName(), "Storing test Student in database");
                Student student = new Student();
                student.setUniqueId(DeviceInfoHelper.getDeviceId(getApplicationContext()) + "_" + i);
                Log.i(getClass().getName(), "student.getUniqueId(): " + student.getUniqueId());
                student.setTimeCreated(Calendar.getInstance());
                student.setAvatar(studentImage);
                studentDao.insert(student);
                Log.i(getClass().getName(), "Student stored in database with id " + student.getId());
            }
            existingStudents = studentDao.loadAll();
        }

        if (existingStudents.isEmpty()) {
            // Redirect to StudentRegistrationActivity
            Intent intent = new Intent(getApplicationContext(), StudentRegistrationActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Redirect to StudentSelectionActivity
            Intent intent = new Intent(getApplicationContext(), StudentSelectionActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
