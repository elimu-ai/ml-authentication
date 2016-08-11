package org.literacyapp.task;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.util.Log;

import java.util.Locale;

public class GraphemeActivity extends AppCompatActivity {

    private ImageView mGraphemeImageView;

    private AudioDao audioDao;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grapheme);

        mGraphemeImageView = (ImageView) findViewById(R.id.graphemeImageView);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        audioDao = literacyApplication.getDaoSession().getAudioDao();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Log.d(getClass(), "tts onInit");

                tts.setLanguage(Locale.US);
            }
        });
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onStart");
        super.onStart();

//        mGraphemeImageView.animate()
//                .setStartDelay(500)
//                .scaleX(2f)
//                .scaleY(2f)
//                .start();

        mGraphemeImageView.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.d(getClass(), "onClick");

//                Audio audio = audioDao.queryBuilder()
//                        .where(AudioDao.Properties.Transcription.eq("'a' phoneme"))
//                        .unique();
//                Log.d(getClass(), "audio: " + audio);
//                Log.d(getClass(), "audio.getId(): " + audio.getId());
//                Log.d(getClass(), "audio.getBytes().length: " + audio.getBytes().length);

//                MediaDataSource mediaDataSource = new ByteArrayMediaDataSource(audio.getBytes());
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setDataSource(mediaDataSource);
//                mediaPlayer.start();

//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.phoneme_a);
//                mediaPlayer.start();

                tts.speak("a", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(getClass(), "onDestroy");
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
