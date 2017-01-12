package org.literacyapp.content.task;

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
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;

public class NumberActivity extends AppCompatActivity {

    private ImageView numberImageView;
    private TextView numberTextView;

    private ImageButton numberNextButton;

    private NumberDao numberDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number);

        numberImageView = (ImageView) findViewById(R.id.numberImageView);
        numberTextView = (TextView) findViewById(R.id.numberTextView);

        numberNextButton = (ImageButton) findViewById(R.id.numberNextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        Integer numberExtra = getIntent().getIntExtra("number", -1);
        Log.i(getClass().getName(), "numberExtra: " + numberExtra);

        numberTextView.setText(numberExtra.toString());

        final Number number = numberDao.queryBuilder()
                .where(NumberDao.Properties.Value.eq(numberExtra))
                .unique();
        Log.i(getClass().getName(), "number: " + number);

        numberImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                playNumberAudio(number);
            }
        }, 2000);

        numberImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                playNumberAudio(number);
            }
        });

        numberNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent graphemeIntent = new Intent(getApplicationContext(), NumberGraphemeActivity.class);
                graphemeIntent.putExtra("number", number.getValue());
                startActivity(graphemeIntent);

                finish();
            }
        });
    }

    private void playNumberAudio(Number number) {
        Log.i(getClass().getName(), "playNumberAudio");

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

        // TODO: animate avatar
    }
}
