package org.literacyapp.content.number;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;

import java.util.List;

public class NumbersActivity extends AppCompatActivity {

    private NumberDao numberDao;

    private GridLayout numberGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.blue));

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        numberDao = literacyApplication.getDaoSession().getNumberDao();

        numberGridLayout = (GridLayout) findViewById(R.id.numberGridLayout);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        List<Number> numbers = numberDao.loadAll();
        Log.i(getClass().getName(), "numbers.size(): " + numbers.size());
        for (Number number : numbers) {
            View numberView = LayoutInflater.from(this).inflate(R.layout.content_numbers_number_view, numberGridLayout, false);
            TextView numberTextView = (TextView) numberView.findViewById(R.id.numberTextView);
            if (TextUtils.isEmpty(number.getSymbol())) {
                numberTextView.setText(number.getValue().toString());
            } else {
                numberTextView.setText(number.getSymbol());
            }

            numberGridLayout.addView(numberView);
        }
    }
}
