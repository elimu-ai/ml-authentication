package org.literacyapp.content.number;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.analytics.eventtracker.EventTracker;
import org.literacyapp.content.task.NumberActivity;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.enums.content.NumeracySkill;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.contentprovider.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {

    private NumberDao numberDao;
    private AudioDao audioDao;

    private GridLayout numberGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.blue));

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();

        numberGridLayout = (GridLayout) findViewById(R.id.numberGridLayout);


        List<Number> numbers = new CurriculumHelper(getApplication()).getAvailableNumbers(null);
        Log.i(getClass().getName(), "numbers.size(): " + numbers.size());
        for (final Number number : numbers) {
            View numberView = LayoutInflater.from(this).inflate(R.layout.content_numbers_number_view, numberGridLayout, false);
            TextView numberTextView = (TextView) numberView.findViewById(R.id.numberTextView);
            if (!TextUtils.isEmpty(number.getSymbol())) {
                numberTextView.setText(number.getSymbol());
            } else {
                numberTextView.setText(number.getValue().toString());
            }

            numberView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    Log.i(getClass().getName(), "number.getValue(): " + number.getValue());
                    Log.i(getClass().getName(), "number.getSymbol(): " + number.getSymbol());
                    Log.i(getClass().getName(), "number.getWord(): " + number.getWord());
                    if (number.getWord() != null) {
                        Log.i(getClass().getName(), "number.getWord().getText(): " + number.getWord().getText());
                    }

                    playNumberSound(number);

                    EventTracker.reportUsageEvent(getApplicationContext(),
                            NumeracySkill.NUMBER_IDENTIFICATION,
                            number.getValue());

                    Intent numberIntent = new Intent(getApplicationContext(), NumberActivity.class);
                    numberIntent.putExtra("number", number.getValue());
                    startActivity(numberIntent);
                }
            });

            numberGridLayout.addView(numberView);
        }
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();


    }

    private void playNumberSound(Number number) {
        Log.i(getClass().getName(), "playNumberSound");

        // Look up corresponding Audio
        Log.d(getClass().getName(), "Looking up \"digit_" + number.getValue() + "\"");
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq("digit_" + number.getValue()))
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
            String audioFileName = "digit_" + number.getValue();
            int resourceId = getResources().getIdentifier(audioFileName, "raw", getPackageName());
            try {
                if (resourceId != 0) {
                    MediaPlayerHelper.play(getApplicationContext(), resourceId);
                } else {
                    // Fall-back to TTS
                    TtsHelper.speak(getApplicationContext(), number.getValue().toString());
                }
            } catch (Resources.NotFoundException e) {
                // Fall-back to TTS
                TtsHelper.speak(getApplicationContext(), number.getValue().toString());
            }
        }
    }
}
