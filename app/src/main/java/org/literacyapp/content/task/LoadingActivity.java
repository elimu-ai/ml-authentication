package org.literacyapp.content.task;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.literacyapp.R;

import pl.droidsonroids.gif.GifImageView;

public class LoadingActivity extends AppCompatActivity {

    private GifImageView loadingGifImageView;

    private ImageButton loadingNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        loadingGifImageView = (GifImageView) findViewById(R.id.loadingGifImageView);
        String[] loadingAnimations = new String[] {
                "loading_colors",
                "loading_dinosaur",
                "loading_dog",
                "loading_fly",
                "loading_jellyfish",
                "loading_sky",
                "loading_space",
                "loading_spring",
                "loading_sun"
        };
        int randomIndex = (int) (Math.random() * loadingAnimations.length);
        String resourceName = loadingAnimations[randomIndex];
        Log.d(getClass().getName(), "resourceName: " + resourceName);
        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        loadingGifImageView.setImageResource(resourceId);

        loadingNextButton = (ImageButton) findViewById(R.id.loadingNextButton);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        loadingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                finish();
            }
        });
    }
}
