package org.literacyapp.authentication.fallback;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

import org.literacyapp.R;
import org.literacyapp.util.MediaPlayerHelper;

public class StudentRegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private RippleBackground rippleBackground;

    private Button cameraButton;

    private ImageView thumbnailImageView;

    private Bitmap imageBitmap;

    private ImageView checkmarkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        rippleBackground = (RippleBackground) findViewById(R.id.studentRegistrationRippleBackground);
        cameraButton = (Button) findViewById(R.id.studentRegistrationButton);
        thumbnailImageView = (ImageView) findViewById(R.id.studentRegistrationImageView);
        checkmarkImageView = (ImageView) findViewById(R.id.studentRegistrationCheckmark);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "cameraButton onClick");

                // Take picture with front camera. See https://developer.android.com/training/camera/photobasics.html
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        if (imageBitmap == null) {

            MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_registration);

            // Play an animation to indicate that the cameraButton should be pressed
            cameraButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(getClass().getName(), "cameraButton.postDelayed run");

                    final long duration = 300;

                    final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(cameraButton, View.SCALE_X, 1f, 1.2f, 1f);
                    scaleXAnimator.setDuration(duration);

                    final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(cameraButton, View.SCALE_Y, 1f, 1.2f, 1f);
                    scaleYAnimator.setDuration(duration);

                    scaleXAnimator.start();
                    scaleYAnimator.start();

                    final AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(scaleXAnimator).with(scaleYAnimator);
                    animatorSet.start();

                    rippleBackground.startRippleAnimation();
                    rippleBackground.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rippleBackground.stopRippleAnimation();
                        }
                    }, 600);
                }
            }, 4000);
        } else {
            // Replace camera button with image thumbnail
            cameraButton.setVisibility(View.GONE);

            // Pulsate image thumbnail
            rippleBackground.startRippleAnimation();
            rippleBackground.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rippleBackground.stopRippleAnimation();
                }
            }, 600);

            // Animate checkmark
            checkmarkImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkmarkImageView.setVisibility(View.VISIBLE);
                    Drawable drawable = checkmarkImageView.getDrawable();
                    ((Animatable) drawable).start();

                    MediaPlayerHelper.play(getApplicationContext(), R.raw.tada);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MediaPlayerHelper.play(getApplicationContext(), R.raw.instruction_student_registration_good_job);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        }
                    }, 1000);
                }
            }, 600);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(getClass().getName(), "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK)) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // TODO: detect face(s) in image
            thumbnailImageView.setImageBitmap(imageBitmap);

            // TODO: store image on SD card
        }
    }
}
