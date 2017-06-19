package org.literacyapp.content.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.literacyapp.R;

public class TagItemsActivity extends AppCompatActivity {

    private ImageView mTagItemsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tag_items);

        getWindow().setStatusBarColor(Color.parseColor("#1C80CF"));

        mTagItemsImageView = (ImageView) findViewById(R.id.tagItemsImageView);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        mTagItemsImageView.animate()
                .setStartDelay(500)
                .rotation(360)
                .scaleX(2)
                .scaleY(2)
                .setDuration(1000)
                .start();

        mTagItemsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onStart");

                mTagItemsImageView.animate()
                        .rotation(mTagItemsImageView.getRotation() + 90)
                        .setDuration(1000)
                        .start();
            }
        });
    }
}
