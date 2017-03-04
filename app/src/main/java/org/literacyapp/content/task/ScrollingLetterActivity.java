package org.literacyapp.content.task;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;

public class ScrollingLetterActivity extends AppCompatActivity {

    private ImageView vanImageView;
    private TextView letterTextView;

    private ImageButton nextButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling_letter);

        vanImageView = (ImageView) findViewById(R.id.vanImageView);
        letterTextView = (TextView) findViewById(R.id.letterTextView);
        nextButton = (ImageButton) findViewById(R.id.nextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        String letterExtra = getIntent().getStringExtra("letter");
        Log.i(getClass().getName(), "letterExtra: " + letterExtra);

        final Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq(letterExtra))
                .unique();
        Log.i(getClass().getName(), "letter: " + letter);

        letterTextView.setText(letter.getText());
        letterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "letterTextView onClick");
                playLetterSound(letter);
            }
        });
        ObjectAnimator letterAnimator = ObjectAnimator.ofFloat(letterTextView, View.TRANSLATION_Y, 0, 20, 0);
        letterAnimator.setDuration(300);
        letterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        letterAnimator.start();

        vanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "letterTextView onClick");
                playLetterSound(letter);
            }
        });
        ObjectAnimator vanAnimator = ObjectAnimator.ofFloat(vanImageView, View.TRANSLATION_Y, 0, 20, 0);
        vanAnimator.setDuration(300);
        vanAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vanAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(letterAnimator).with(vanAnimator);
        animatorSet.start();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent visemeIntent = new Intent(getApplicationContext(), VisemeActivity.class);
                visemeIntent.putExtra("letter", letter.getText());
                startActivity(visemeIntent);

                finish();
            }
        });
    }

    private void playLetterSound(Letter letter) {
        Log.i(getClass().getName(), "playLetterSound");

        // Look up corresponding Audio
        Log.d(getClass().getName(), "Looking up \"letter_sound_" + letter.getText() + "\"");
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq("letter_sound_" + letter.getText()))
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
        } else {
            // Audio not found. Fall-back to application resource.
            String audioFileName = "letter_sound_" + letter.getText();
            int resourceId = getResources().getIdentifier(audioFileName, "raw", getPackageName());
            try {
                if (resourceId != 0) {
                    MediaPlayerHelper.play(getApplicationContext(), resourceId);
                } else {
                    // Fall-back to TTS
                    TtsHelper.speak(getApplicationContext(), letter.getText());
                }
            } catch (Resources.NotFoundException e) {
                // Fall-back to TTS
                TtsHelper.speak(getApplicationContext(), letter.getText());
            }
        }
    }
}
