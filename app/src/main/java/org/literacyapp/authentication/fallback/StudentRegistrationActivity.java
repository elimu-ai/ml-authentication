package org.literacyapp.authentication.fallback;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.literacyapp.R;
import org.literacyapp.util.MediaPlayerHelper;

public class StudentRegistrationActivity extends AppCompatActivity {

    private Button studentRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        studentRegistrationButton = (Button) findViewById(R.id.studentRegistrationButton);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_registration);

        // Play an animation to indicate that the button should be pressed
        studentRegistrationButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                final long duration = 300;

                final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(studentRegistrationButton, View.SCALE_X, 1f, 1.2f, 1f);
                scaleXAnimator.setDuration(duration);
                scaleXAnimator.setRepeatCount(1);

                final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(studentRegistrationButton, View.SCALE_Y, 1f, 1.2f, 1f);
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
}
