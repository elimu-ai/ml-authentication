package org.literacyapp.content.letter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.Letter;
import org.literacyapp.dao.LetterDao;

import java.util.List;

public class LettersActivity extends AppCompatActivity {

    private LetterDao letterDao;

    private GridLayout letterGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();

        letterGridLayout = (GridLayout) findViewById(R.id.letterGridLayout);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        List<Letter> letters = letterDao.loadAll();
        Log.i(getClass().getName(), "letters.size(): " + letters.size());
        for (Letter letter : letters) {
            View letterView = LayoutInflater.from(this).inflate(R.layout.content_letters_letter_view, letterGridLayout, false);
            TextView letterTextView = (TextView) letterView.findViewById(R.id.letterTextView);
            letterTextView.setText(letter.getText());
            letterGridLayout.addView(letterView);
        }
    }
}
