package org.literacyapp.task;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.literacyapp.R;

public class LessonCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_complete);
    }


}
