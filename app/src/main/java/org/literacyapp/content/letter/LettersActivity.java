package org.literacyapp.content.letter;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.analytics.eventtracker.EventTracker;
import org.literacyapp.content.task.ScrollingLetterActivity;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.contentprovider.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;
import java.util.List;

public class LettersActivity extends AppCompatActivity {

    private LetterDao letterDao;
    private AudioDao audioDao;

    private GridLayout letterGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();

        letterGridLayout = (GridLayout) findViewById(R.id.letterGridLayout);


        List<Letter> letters = new CurriculumHelper(getApplication()).getAvailableLetters(null);
        Log.i(getClass().getName(), "letters.size(): " + letters.size());
        for (final Letter letter : letters) {
            View letterView = LayoutInflater.from(this).inflate(R.layout.content_letters_letter_view, letterGridLayout, false);
            TextView letterTextView = (TextView) letterView.findViewById(R.id.letterTextView);
            letterTextView.setText(letter.getText());

            letterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    playLetterSound(letter);

                    EventTracker.reportUsageEvent(getApplicationContext(),
                            LiteracySkill.LETTER_IDENTIFICATION,
                            letter.getText());

                    Intent scrollingLetterIntent = new Intent(getApplicationContext(), ScrollingLetterActivity.class);
                    scrollingLetterIntent.putExtra("letter", letter.getText());
                    startActivity(scrollingLetterIntent);
                }
            });

            letterGridLayout.addView(letterView);
        }
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();


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
