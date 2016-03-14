package org.literacyapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import org.literacyapp.util.Log;

import java.io.File;
import java.io.FileInputStream;

public class ParallaxActivity extends AppCompatActivity {

    private HorizontalScrollView mHorizontalScrollView;

    private ImageView mImageViewParallaxLayer1;
    private ImageView mImageViewParallaxLayer2;
    private ImageView mImageViewParallaxLayer3;
    private ImageView mImageViewParallaxLayer4;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);

        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewParallax);
        mImageViewParallaxLayer1 = (ImageView) findViewById(R.id.imageViewParallaxLayer1);
        mImageViewParallaxLayer2 = (ImageView) findViewById(R.id.imageViewParallaxLayer2);
        mImageViewParallaxLayer3 = (ImageView) findViewById(R.id.imageViewParallaxLayer3);
        mImageViewParallaxLayer4 = (ImageView) findViewById(R.id.imageViewParallaxLayer4);
    }

    @Override
    protected void onStart() {
        Log.d(getClass().getName(), "onCreate");
        super.onStart();

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_forest);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        mHorizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d(getClass().getName(), "onScrollChanged");

                int scrollX = mHorizontalScrollView.getScrollX();
                Log.d(getClass().getName(), "scrollX: " + scrollX);

                mImageViewParallaxLayer1.setX(scrollX / 2);
                mImageViewParallaxLayer1.setY(scrollX);

                mImageViewParallaxLayer2.setX(scrollX / 4);
                mImageViewParallaxLayer2.setY(scrollX / 2);

                mImageViewParallaxLayer3.setX(scrollX / 8);
                mImageViewParallaxLayer3.setY(scrollX / 16);

                mImageViewParallaxLayer4.setX(scrollX / 32);
            }
        });
    }

    @Override
    protected void onStop() {
        Log.d(getClass().getName(), "onCreate");
        super.onStop();

        mMediaPlayer.release();
    }
}
