package org.literacyapp.task;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Audio;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.Letter;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.util.Log;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;

public class GraphemeActivity extends AppCompatActivity {

    private ImageView mGraphemeImageView;

    private ImageButton mGraphemeImageButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grapheme);

        mGraphemeImageView = (ImageView) findViewById(R.id.graphemeImageView);

        mGraphemeImageButton = (ImageButton) findViewById(R.id.graphemeImageButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onStart");
        super.onStart();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq("a")) // TODO: fetch value dynamically
                .unique();
        Log.d(getClass(), "letter: " + letter);

        // Look up corresponding audio
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq(letter.getText()))
                .unique();
        Log.d(getClass(), "audio: " + audio);

        if (audio != null) {
            // Play audio
            File audioFile = MultimediaHelper.getFile(audio);
            Uri uri = Uri.parse(audioFile.getAbsolutePath());
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.d(getClass(), "audio: " + audio);
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        }

        mGraphemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.d(getClass(), "onClick");

                if (audio != null) {
                    // Play audio
                    File audioFile = MultimediaHelper.getFile(audio);
                    Uri uri = Uri.parse(audioFile.getAbsolutePath());
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Log.d(getClass(), "audio: " + audio);
                            mediaPlayer.release();
                        }
                    });
                    mediaPlayer.start();
                }
            }
        });

        mGraphemeImageButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play a subtle animation
                final long duration = 300;

                final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mGraphemeImageButton, View.SCALE_X, 1f, 1.2f, 1f);
                scaleXAnimator.setDuration(duration);
                scaleXAnimator.setRepeatCount(1);

                final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mGraphemeImageButton, View.SCALE_Y, 1f, 1.2f, 1f);
                scaleYAnimator.setDuration(duration);
                scaleYAnimator.setRepeatCount(1);

                scaleXAnimator.start();
                scaleYAnimator.start();

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleXAnimator).with(scaleYAnimator);
                animatorSet.start();
            }
        }, 2000);

        Drawable drawable = mGraphemeImageView.getDrawable();
        ((Animatable) drawable).start();

        mGraphemeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass(), "onClick");

                Intent starIntent = new Intent(getApplicationContext(), StarActivity.class);
                startActivity(starIntent);
            }
        });
    }
}
