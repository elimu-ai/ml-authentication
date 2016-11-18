package org.literacyapp.content.letter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.LetterDao;

public class LettersActivity extends AppCompatActivity {

    private LetterDao letterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
    }


}
