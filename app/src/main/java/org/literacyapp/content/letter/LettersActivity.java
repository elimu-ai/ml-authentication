package org.literacyapp.content.letter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.content.Letter;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.TtsHelper;

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


        List<Letter> letters = new CurriculumHelper(getApplication()).getAvailableLetters(null);
        Log.i(getClass().getName(), "letters.size(): " + letters.size());
        for (final Letter letter : letters) {
            View letterView = LayoutInflater.from(this).inflate(R.layout.content_letters_letter_view, letterGridLayout, false);
            TextView letterTextView = (TextView) letterView.findViewById(R.id.letterTextView);
            letterTextView.setText(letter.getText());

            letterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    String audioFileName = "letter_" + letter.getText();
                    int resourceId = getResources().getIdentifier(audioFileName, "raw", getPackageName());
                    try {
                        if (resourceId != 0) {
                            MediaPlayerHelper.play(getApplicationContext(), resourceId);
                        } else {
                            // Fall-back to TTS
                            TtsHelper.speak(getApplicationContext(), letter.getText());
                        }
                    } catch (Resources.NotFoundException e) {
                        // Fall-back to TTS
                        TtsHelper.speak(getApplicationContext(), letter.getText());
                    }

                    Intent intent = new Intent();
                    intent.setPackage("org.literacyapp.analytics");
                    intent.setAction("literacyapp.intent.action.USAGE_EVENT");
                    intent.putExtra("packageName", getPackageName());
                    intent.putExtra("literacySkill", "LETTER_IDENTIFICATION");
                    sendBroadcast(intent);

//                    Intent visemeIntent = new Intent(getApplicationContext(), VisemeActivity.class);
//                    startActivity(visemeIntent);
                }
            });

            letterGridLayout.addView(letterView);
        }
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();


    }
}
