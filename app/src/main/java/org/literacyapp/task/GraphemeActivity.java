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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.model.content.Letter;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;

public class GraphemeActivity extends AppCompatActivity {

    private ImageView mGraphemeImageView;

    private ImageButton mGraphemeImageButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
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
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq("a")) // TODO: fetch value dynamically
                .unique();
        Log.i(getClass().getName(), "letter: " + letter);

        // Look up corresponding audio
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq(letter.getText()))
                .unique();
        Log.i(getClass().getName(), "audio: " + audio);

        if (audio != null) {
            // Play audio
            File audioFile = MultimediaHelper.getFile(audio);
            Uri uri = Uri.parse(audioFile.getAbsolutePath());
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.i(getClass().getName(), "onCompletion");
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        }

        mGraphemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                if (audio != null) {
                    // Play audio
                    File audioFile = MultimediaHelper.getFile(audio);
                    Uri uri = Uri.parse(audioFile.getAbsolutePath());
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Log.i(getClass().getName(), "onCompletion");
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
                Log.i(getClass().getName(), "onClick");

                Intent starIntent = new Intent(getApplicationContext(), StarActivity.class);
                startActivity(starIntent);
            }
        });
    }
}
