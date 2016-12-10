package org.literacyapp.authentication.fallback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.literacyapp.R;
import org.literacyapp.util.MediaPlayerHelper;

public class StudentRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_registration);


    }
}
