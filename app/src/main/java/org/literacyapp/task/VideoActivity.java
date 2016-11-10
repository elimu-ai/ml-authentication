package org.literacyapp.task;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Video;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.util.Log;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    private VideoDao videoDao;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        videoDao = literacyApplication.getDaoSession().getVideoDao();

        videoView = (VideoView) findViewById(R.id.videoView);
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onStart");
        super.onStart();

//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        List<Video> videos = videoDao.loadAll(); // TODO: load video matching student's current level
        Log.d(getClass(), "videos.size(): " + videos.size());
        Video video = videos.get(0);
        File videoFile = MultimediaHelper.getFile(video);
        Log.d(getClass(), "videoFile: " + videoFile);

        videoView.setVideoPath(videoFile.getAbsolutePath());
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d(getClass(), "onCompletion");

                // TODO: take winner picture for completing lesson
            }
        });
    }
}
