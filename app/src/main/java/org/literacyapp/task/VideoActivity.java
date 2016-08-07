package org.literacyapp.task;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import org.apache.commons.io.IOUtils;
import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Video;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        List<Video> videos = videoDao.loadAll(); // TODO: load video matching student's current level
        Log.d(getClass(), "videos.size(): " + videos.size());
        Video video = videos.get(0);
        Log.d(getClass(), "video.getBytes().length: " + video.getBytes().length);

        File videoDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "LiteracyApp" + File.separator + "multimedia" + File.separator + "video");
        Log.d(getClass(), "videoDirectory.getAbsolutePath(): " + videoDirectory.getAbsolutePath());
        Log.d(getClass(), "videoDirectory.exists(): " + videoDirectory.exists());
        if (!videoDirectory.exists()) {
            videoDirectory.mkdirs();
        }

        // Store file
        String videoPath = videoDirectory + File.separator + video.getId() + "_r" + video.getRevisionNumber() + "." + video.getVideoFormat().toString().toLowerCase();
        Log.d(getClass(), "videoPath: " + videoPath);
        Log.d(getClass(), "videoPath.exists(): " + new File(videoPath).exists());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(videoPath);
            IOUtils.write(video.getBytes(), fileOutputStream);
            videoView.setVideoPath(videoPath);
            videoView.setMediaController(new MediaController(this));
            videoView.start();
        } catch (FileNotFoundException e) {
            Log.e(getClass(), null, e);
        } catch (IOException e) {
            Log.e(getClass(), null, e);
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d(getClass(), "onCompletion");

                // TODO: take winner picture for completing lesson
            }
        });
    }
}
