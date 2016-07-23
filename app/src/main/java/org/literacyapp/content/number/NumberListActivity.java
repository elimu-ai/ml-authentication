package org.literacyapp.content.number;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Number;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.util.Log;

import java.util.List;

public class NumberListActivity extends AppCompatActivity {

    private TextView mTextViewNumberList;

    private NumberDao numberDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_list);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplication();
        numberDao = literacyApplication.getDaoSession().getNumberDao();

        mTextViewNumberList = (TextView) findViewById(R.id.textViewNumberList);
    }

    @Override
    protected void onStart() {
        Log.d(getClass(), "onStart");
        super.onStart();

        String numberListText = "";
        List<Number> numbers = numberDao.loadAll();
        Log.d(getClass(), "numbers.size(): " + numbers.size());
        for (Number number : numbers) {
            numberListText += "id: " + number.getId() + ", language: " + number.getLocale().getLanguage() + ",  value: " + number.getValue() + ", word: " + number.getWord() + "\n";
        }
        mTextViewNumberList.setText(numberListText);
    }
}
