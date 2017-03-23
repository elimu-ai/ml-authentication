package org.literacyapp.content.task;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.contentprovider.dao.VideoDao;
import org.literacyapp.contentprovider.model.content.multimedia.Video;
import org.literacyapp.contentprovider.util.MultimediaHelper;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_VIDEO_ID = "videoId";

    private VideoDao videoDao;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        videoDao = literacyApplication.getDaoSession().getVideoDao();

        videoView = (VideoView) findViewById(R.id.videoView);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        Bundle extras = getIntent().getExtras();
        long videoId = extras.getLong(EXTRA_KEY_VIDEO_ID);
        Log.i(getClass().getName(), "videoId: " + videoId);

        Video video = videoDao.load(videoId);
        File videoFile = MultimediaHelper.getFile(video);
        Log.i(getClass().getName(), "videoFile: " + videoFile);

        videoView.setVideoPath(videoFile.getAbsolutePath());
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(getClass().getName(), "onCompletion");

                // TODO: track VideoCompletedEvent

                Intent loadingIntent = new Intent(getApplicationContext(), LoadingActivity.class);
//                String letter = "a"; // TODO: fetch dynamically
//                loadingIntent.putExtra(LoadingActivity.EXTRA_KEY_LETTER, letter);
                startActivity(loadingIntent);

                finish();
            }
        });
    }
}
