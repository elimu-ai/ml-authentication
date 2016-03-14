package org.literacyapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import org.literacyapp.util.Log;

public class RoomActivity extends AppCompatActivity {

    private HorizontalScrollView mHorizontalScrollViewRoom;

    private ImageView mImageViewRoom;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mHorizontalScrollViewRoom = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewRoom);
        mImageViewRoom = (ImageView) findViewById(R.id.imageViewRoom);
    }

    @Override
    protected void onStart() {
        Log.d(getClass().getName(), "onCreate");
        super.onStart();

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music_blues);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onStop() {
        Log.d(getClass().getName(), "onCreate");
        super.onStop();

        mMediaPlayer.release();
    }
}
