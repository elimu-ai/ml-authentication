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
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.model.content.Number;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.contentprovider.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;

public class NumberGraphemeActivity extends AppCompatActivity {

    private ImageView graphemeImageView;

    private ImageButton graphemeNextButton;

    private NumberDao numberDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_grapheme);

        graphemeImageView = (ImageView) findViewById(R.id.graphemeImageView);

        graphemeNextButton = (ImageButton) findViewById(R.id.loadingNextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        MediaPlayerHelper.play(getApplicationContext(), R.raw.activity_instruction_number_grapheme);

        Integer numberExtra = getIntent().getIntExtra("number", -1);
        Log.i(getClass().getName(), "numberExtra: " + numberExtra);

        final Number number = numberDao.queryBuilder()
                .where(NumberDao.Properties.Value.eq(numberExtra))
                .unique();
        Log.i(getClass().getName(), "number: " + number);

        drawNumber(number);

        graphemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                drawNumber(number);
            }
        });

        graphemeNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent intent = new Intent(getApplicationContext(), SelectNumberActivity.class);
                intent.putExtra("number", number.getValue());
                startActivity(intent);

                finish();
            }
        });
    }

    private void drawNumber(final Number number) {
        Log.i(getClass().getName(), "drawNumber");

        int drawableResourceIdStroke1 = getResources().getIdentifier("animated_number_" + number.getValue(), "drawable", getPackageName());
        final Drawable drawableStroke1 = getDrawable(drawableResourceIdStroke1);
        graphemeImageView.setImageDrawable(drawableStroke1);

        try {
            // Check if more than 1 strokes

            Log.i(getClass().getName(), "Looking up resource: " + "animated_number_" + number.getValue() + "_stroke2");

            int drawableResourceIdStroke2 = getResources().getIdentifier("animated_number_" + number.getValue() + "_stroke2", "drawable", getPackageName());
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
                            playNumberSound(number);
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
                    playNumberSound(number);
                }
            }, 5000);
        }
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
