package org.literacyapp.content.multimedia.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.model.content.multimedia.Video;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;
import java.util.List;

public class VideosActivity extends AppCompatActivity {

    private VideoDao videoDao;

    private GridLayout videoGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.purple));

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        videoDao = literacyApplication.getDaoSession().getVideoDao();

        videoGridLayout = (GridLayout) findViewById(R.id.videoGridLayout);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        List<Video> videos = videoDao.loadAll();
        Log.i(getClass().getName(), "videos.size(): " + videos.size());
        for (final Video video : videos) {
            View videoView = LayoutInflater.from(this).inflate(R.layout.content_videos_video_view, videoGridLayout, false);

            File videoThumbnailFile = MultimediaHelper.getThumbnail(video);
            if (videoThumbnailFile.exists()) {
                ImageView videoImageView = (ImageView) videoView.findViewById(R.id.videoImageView);
                Bitmap bitmap = BitmapFactory.decodeFile(videoThumbnailFile.getAbsolutePath());
                videoImageView.setImageBitmap(bitmap);
            }

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    Log.i(getClass().getName(), "video.getId(): " + video.getId());
                    Log.i(getClass().getName(), "video.getTitle(): " + video.getTitle());

                    // TODO: open VideoActivity
                }
            });

            videoGridLayout.addView(videoView);
        }
    }
}
