package org.literacyapp.content.task;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;

public class VisemeActivity extends AppCompatActivity {

    private ImageView visemeImageView;
    private TextView visemeTextView;

    private ImageButton nextButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viseme);

        visemeImageView = (ImageView) findViewById(R.id.visemeImageView);
        visemeTextView = (TextView) findViewById(R.id.visemeTextView);

        nextButton = (ImageButton) findViewById(R.id.visemeImageButton);

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

        visemeTextView.setText(letterExtra);

        final Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq(letterExtra))
                .unique();
        Log.i(getClass().getName(), "letter: " + letter);

        visemeImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                playLetterSound(letter);
            }
        }, 2000);

        visemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                playLetterSound(letter);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent graphemeIntent = new Intent(getApplicationContext(), GraphemeActivity.class);
                graphemeIntent.putExtra("letter", letter.getText());
                startActivity(graphemeIntent);

                finish();
            }
        });
    }

    private void playLetterSound(Letter letter) {
        Log.i(getClass().getName(), "playLetterSound");

        // Look up corresponding Audio
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
        } else {
            // Audio not found. Fall-back to application resource.
            String audioFileName = "letter_" + letter.getText();
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

        if ("a".equals(letter.getText())) {
            // Morph to viseme
            visemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_penguin_neutral_to_a_animated_vector));
            Drawable drawable = visemeImageView.getDrawable();
            ((Animatable) drawable).start();

            visemeImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Morph back to neutral
                    visemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_penguin_a_to_neutral_animated_vector));
                    Drawable drawable = visemeImageView.getDrawable();
                    ((Animatable) drawable).start();
                }
            }, 500);
        } else {
            // Morph to viseme
            visemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_penguin_neutral_to_t_animated_vector));
            Drawable drawable = visemeImageView.getDrawable();
            ((Animatable) drawable).start();

            visemeImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Morph back to neutral
                    visemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_penguin_t_to_neutral_animated_vector));
                    Drawable drawable = visemeImageView.getDrawable();
                    ((Animatable) drawable).start();
                }
            }, 500);
        }
    }
}
