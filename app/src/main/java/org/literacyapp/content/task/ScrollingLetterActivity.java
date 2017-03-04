package org.literacyapp.content.task;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.literacyapp.R;
import org.literacyapp.contentprovider.dao.NumberDao;
import org.literacyapp.contentprovider.model.content.Number;

public class ScrollingLetterActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_LETTER = "letter";

    private NumberDao numberDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);


    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        int numberExtra = getIntent().getIntExtra("number", -1);
        Log.i(getClass().getName(), "numberExtra: " + numberExtra);

        final Number number = numberDao.queryBuilder()
                .where(NumberDao.Properties.Value.eq(numberExtra))
                .unique();
        Log.i(getClass().getName(), "number: " + number);
    }
}
