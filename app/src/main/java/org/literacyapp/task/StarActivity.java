package org.literacyapp.task;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.R;

public class StarActivity extends AppCompatActivity {

    private ImageView mStarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_star);

        mStarImageView = (ImageView) findViewById(R.id.starImageView);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        Drawable drawable = mStarImageView.getDrawable();
        ((Animatable) drawable).start();

        mStarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Drawable drawable = mStarImageView.getDrawable();
                ((Animatable) drawable).start();
            }
        });
    }
}
