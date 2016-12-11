package org.literacyapp.authentication.fallback;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.model.Student;

import java.io.File;
import java.util.List;

public class StudentSelectionActivity extends AppCompatActivity {

    private StudentDao studentDao;

    private GridLayout studentSelectionGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_selection);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplication();
        studentDao = literacyApplication.getDaoSession().getStudentDao();

        studentSelectionGridLayout = (GridLayout) findViewById(R.id.studentSelectionGridLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "fab onClick");

                Intent intent = new Intent(getApplicationContext(), StudentRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        // List existing Students
        List<Student> students = studentDao.loadAll();
        Log.i(getClass().getName(), "students.size(): " + students.size());
        for (final Student student : students) {
            View studentView = LayoutInflater.from(this).inflate(R.layout.content_student_selection_view, studentSelectionGridLayout, false);

            File studentAvatar = new File(student.getAvatar().getImageFileUrl());
            ImageView studentImageView = (ImageView) studentView.findViewById(R.id.studentSelectionImageView);
            Bitmap bitmap = BitmapFactory.decodeFile(studentAvatar.getAbsolutePath());
            studentImageView.setImageBitmap(bitmap);

            studentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    Log.i(getClass().getName(), "student.getUniqueId(): " + student.getUniqueId());
                    // TODO: personalize apps/content according to Student's level

                    finish();
                }
            });

            studentSelectionGridLayout.addView(studentView);
        }
    }
}
