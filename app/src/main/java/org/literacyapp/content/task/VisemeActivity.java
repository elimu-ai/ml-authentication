package org.literacyapp.content.task;

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
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;

public class VisemeActivity extends AppCompatActivity {

    private ImageView mVisemeImageView;

    private ImageButton mVisemeImageButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viseme);

        mVisemeImageView = (ImageView) findViewById(R.id.visemeImageView);

        mVisemeImageButton = (ImageButton) findViewById(R.id.visemeImageButton);

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

        mVisemeImageView.setOnClickListener(new View.OnClickListener() {

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

                // Morph to 't'
                mVisemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_neutral_to_t_animated_vector));
                Drawable drawable = mVisemeImageView.getDrawable();
                ((Animatable) drawable).start();

                mVisemeImageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Morph back to neutral
                        mVisemeImageView.setImageDrawable(getResources().getDrawable(R.drawable.viseme_t_to_neutral_animated_vector));
                        Drawable drawable = mVisemeImageView.getDrawable();
                        ((Animatable) drawable).start();
                    }
                }, 500);
            }
        });

        mVisemeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");


            }
        });
    }
}
