package org.literacyapp.task;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.literacyapp.R;

public class TagItemsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_tag_items);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        // TODO
    }
}
