package org.literacyapp.task;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Video;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

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

        List<Video> videos = videoDao.loadAll(); // TODO: load video matching student's current level
        Log.i(getClass().getName(), "videos.size(): " + videos.size());
        Video video = videos.get(0);
        File videoFile = MultimediaHelper.getFile(video);
        Log.i(getClass().getName(), "videoFile: " + videoFile);

        videoView.setVideoPath(videoFile.getAbsolutePath());
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(getClass().getName(), "onCompletion");

                // TODO: take winner picture for completing lesson
            }
        });
    }
}
