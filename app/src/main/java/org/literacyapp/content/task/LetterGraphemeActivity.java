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

public class LetterGraphemeActivity extends AppCompatActivity {

    private ImageView graphemeImageView;

    private ImageButton graphemeNextButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_letter_grapheme);

        graphemeImageView = (ImageView) findViewById(R.id.graphemeImageView);

        graphemeNextButton = (ImageButton) findViewById(R.id.loadingNextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        MediaPlayerHelper.play(getApplicationContext(), R.raw.activity_instruction_letter_grapheme);

        String letterExtra = getIntent().getStringExtra("letter");
        Log.i(getClass().getName(), "letterExtra: " + letterExtra);

        final Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq(letterExtra))
                .unique();
        Log.i(getClass().getName(), "letter: " + letter);

        drawLetter(letter);

        graphemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                drawLetter(letter);
            }
        });

        graphemeNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent intent = new Intent(getApplicationContext(), SelectLetterActivity.class);
                intent.putExtra("letter", letter.getText());
                startActivity(intent);

                finish();
            }
        });
    }

    private void drawLetter(final Letter letter) {
        Log.i(getClass().getName(), "drawLetter");

        int drawableResourceIdStroke1 = getResources().getIdentifier("animated_letter_" + letter.getText(), "drawable", getPackageName());
        final Drawable drawableStroke1 = getDrawable(drawableResourceIdStroke1);
        graphemeImageView.setImageDrawable(drawableStroke1);

        try {
            // Check if more than 1 strokes

            Log.i(getClass().getName(), "Looking up resource: " + "animated_letter_" + letter.getText() + "_stroke2");

            int drawableResourceIdStroke2 = getResources().getIdentifier("animated_letter_" + letter.getText() + "_stroke2", "drawable", getPackageName());
            final Drawable drawableStroke2 = getDrawable(drawableResourceIdStroke2);

            Log.i(getClass().getName(), "2 strokes");

            ((Animatable) drawableStroke1).start();

            graphemeImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    graphemeImageView.setImageDrawable(drawableStroke2);
                    ((Animatable) drawableStroke2).start();

                    graphemeImageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playLetterSound(letter);
                        }
                    }, 2500);
                }
            }, 5000);
        } catch (Resources.NotFoundException e) {
            // Only 1 stroke

            Log.i(getClass().getName(), "1 stroke");

            ((Animatable) drawableStroke1).start();

            graphemeImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playLetterSound(letter);
                }
            }, 5000);
        }
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
