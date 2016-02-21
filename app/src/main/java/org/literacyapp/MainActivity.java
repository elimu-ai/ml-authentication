package org.literacyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String android_id;
    private Button mButtonID;
    private TextView mTextViewID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonID = (Button) findViewById(R.id.unique_id_button);
        mTextViewID = (TextView) findViewById(R.id.unique_id_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);

        mButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewID.setText(android_id);
            }
        });

    }
}
