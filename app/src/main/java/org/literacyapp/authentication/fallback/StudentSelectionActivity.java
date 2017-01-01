package org.literacyapp.authentication.fallback;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.authentication.helper.AuthenticationHelper;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.model.Student;
import org.literacyapp.receiver.ScreenOnReceiver;
import org.literacyapp.util.MediaPlayerHelper;

import java.io.File;
import java.util.Calendar;
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
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        // List existing Students
        List<Student> students = studentDao.loadAll();
        Log.i(getClass().getName(), "students.size(): " + students.size());
        studentSelectionGridLayout.removeAllViews();
        for (final Student student : students) {
            View studentView = LayoutInflater.from(this).inflate(R.layout.content_student_selection_view, studentSelectionGridLayout, false);

            File studentAvatar = new File(student.getAvatar());
            ImageView studentImageView = (ImageView) studentView.findViewById(R.id.studentSelectionImageView);
            Bitmap bitmap = BitmapFactory.decodeFile(studentAvatar.getAbsolutePath());
            studentImageView.setImageBitmap(bitmap);

            studentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    Log.i(getClass().getName(), "student.getUniqueId(): " + student.getUniqueId());
                    // Personalize apps/content according to Student's level
                    AuthenticationHelper.updateCurrentStudent(student, getApplicationContext(), true);

                    // Store time of last successful authentication
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putLong(ScreenOnReceiver.PREF_TIME_OF_LAST_AUTHENTICATION, Calendar.getInstance().getTimeInMillis()).commit();

                    finish();
                }
            });

            studentSelectionGridLayout.addView(studentView);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "fab onClick");

                Intent intent = new Intent(getApplicationContext(), StudentRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_selection_can_you_find_yourself);

                fab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_selection_cannot);

                        fab.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Animate button to indicate that it can be pressed
                                final long duration = 300;

                                final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 1f, 1.2f, 1f);
                                scaleXAnimator.setDuration(duration);
                                scaleXAnimator.setRepeatCount(1);

                                final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 1f, 1.2f, 1f);
                                scaleYAnimator.setDuration(duration);
                                scaleYAnimator.setRepeatCount(1);

                                scaleXAnimator.start();
                                scaleYAnimator.start();

                                final AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.play(scaleXAnimator).with(scaleYAnimator);
                                animatorSet.start();
                            }
                        }, 4000);
                    }
                }, 5000);
            }
        }, 2000);
    }
}
