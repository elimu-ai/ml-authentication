package org.literacyapp.content.multimedia.video;

import android.content.Intent;
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
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.multimedia.Video;
import org.literacyapp.util.MultimediaHelper;

import java.io.File;
import java.util.Iterator;
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


        List<Letter> availableLetters = new CurriculumHelper(getApplication()).getAvailableLetters(null);
        List<Number> availableNumbers = new CurriculumHelper(getApplication()).getAvailableNumbers(null);
        List<Video> videos = videoDao.loadAll();
        Log.i(getClass().getName(), "videos.size(): " + videos.size());

        // Hide Videos containing material not yet available to the Student
        Iterator<Video> videoIterator = videos.iterator();
        while (videoIterator.hasNext()) {
            Video video = videoIterator.next();

            List<Letter> lettersInVideo = video.getLetters();
            List<Number> numbersInVideo = video.getNumbers();

            if (!lettersInVideo.isEmpty() || !numbersInVideo.isEmpty()) {
                // Hide the video if it _only_ contains Letters not yet made available to the Student
                boolean videoContainsAtLeastOneAvailableLetter = false;
                for (Letter letter : lettersInVideo) {
                    if (availableLetters.contains(letter)) {
                        videoContainsAtLeastOneAvailableLetter = true;
                        break;
                    }
                }

                // Hide the video if it _only_ contains Numbers not yet made available to the Student
                boolean videoContainsAtLeastOneAvailableNumber = false;
                for (Number number : numbersInVideo) {
                    if (availableNumbers.contains(number)) {
                        videoContainsAtLeastOneAvailableNumber = true;
                        break;
                    }
                }

                if (!videoContainsAtLeastOneAvailableLetter && !videoContainsAtLeastOneAvailableNumber) {
                    videoIterator.remove();
                }
            }
        }

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

                    Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                    intent.putExtra(VideoActivity.EXTRA_KEY_VIDEO_ID, video.getId());
                    startActivity(intent);
                }
            });

            videoGridLayout.addView(videoView);
        }
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();


    }
}
