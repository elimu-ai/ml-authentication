package org.literacyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.literacyapp.util.DeviceIdHelper;

public class MainActivity extends AppCompatActivity {

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

        mButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewID.setText(DeviceIdHelper.getDeviceid(getApplicationContext()));
            }
        });

    }
}
