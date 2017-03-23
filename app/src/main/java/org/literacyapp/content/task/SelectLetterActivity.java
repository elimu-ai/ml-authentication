package org.literacyapp.content.task;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.skyfishjy.library.RippleBackground;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.contentprovider.dao.AudioDao;
import org.literacyapp.contentprovider.dao.LetterDao;
import org.literacyapp.contentprovider.model.Student;
import org.literacyapp.contentprovider.model.content.Letter;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.contentprovider.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectLetterActivity extends AppCompatActivity {

    private RippleBackground rippleBackground;
    private Button listenButton;
    private Button alt1Button;
    private Button alt2Button;

    private ImageButton nextButton;

    private LetterDao letterDao;
    private AudioDao audioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_letter);

        rippleBackground = (RippleBackground) findViewById(R.id.rippleBackground);
        listenButton = (Button) findViewById(R.id.listenButton);
        alt1Button = (Button) findViewById(R.id.alt1Button);
        alt2Button = (Button) findViewById(R.id.alt2Button);

        nextButton = (ImageButton) findViewById(R.id.nextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        letterDao = literacyApplication.getDaoSession().getLetterDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        String letterExtra = getIntent().getStringExtra("letter");
        Log.i(getClass().getName(), "letterExtra: " + letterExtra);

        final Letter letter = letterDao.queryBuilder()
                .where(LetterDao.Properties.Text.eq(letterExtra))
                .unique();
        Log.i(getClass().getName(), "letter: " + letter);

        Student student = null;
        // TODO: get current student
        List<Letter> availableLetters = new CurriculumHelper(getApplicationContext()).getAvailableLetters(student);
        ArrayList<String> availableLettersStringArrayList = new ArrayList<>();
        for (Letter availableLetter : availableLetters) {
            availableLettersStringArrayList.add(availableLetter.getText());
        }
        Log.i(getClass().getName(), "availableLettersStringArrayList: " + availableLettersStringArrayList);
        availableLettersStringArrayList.remove(letter.getText());
        Log.i(getClass().getName(), "availableLettersStringArrayList (after removing current letter): " + availableLettersStringArrayList);
        int randomIndex = (int) (Math.random() * availableLettersStringArrayList.size());
        String wrongLetterAlternative = availableLettersStringArrayList.get(randomIndex);
        Log.i(getClass().getName(), "wrongLetterAlternative: " + wrongLetterAlternative);
        boolean isAlt1Correct = (Math.random() > .5);
        if (isAlt1Correct) {
            alt1Button.setText(letter.getText());
            alt1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alt1Button.setTextColor(Color.WHITE);
                    alt1Button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    alt2Button.setEnabled(false);
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_correct);
                    alt1Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playLetterSound(letter);

                            rippleBackground.startRippleAnimation();
                            rippleBackground.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rippleBackground.stopRippleAnimation();
                                    nextButton.setVisibility(View.VISIBLE);
                                }
                            }, 600);
                        }
                    }, 1500);
                }
            });

            alt2Button.setText(wrongLetterAlternative);
            alt2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_incorrect);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                    alt2Button.startAnimation(animation);
                    alt2Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playLetterSound(letter);

                            rippleBackground.startRippleAnimation();
                            rippleBackground.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rippleBackground.stopRippleAnimation();
                                }
                            }, 600);
                        }
                    }, 1000);
                }
            });
        } else {
            alt2Button.setText(letter.getText());
            alt2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alt2Button.setTextColor(Color.WHITE);
                    alt2Button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    alt1Button.setEnabled(false);
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_correct);
                    alt2Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playLetterSound(letter);

                            rippleBackground.startRippleAnimation();
                            rippleBackground.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rippleBackground.stopRippleAnimation();
                                    nextButton.setVisibility(View.VISIBLE);
                                }
                            }, 600);
                        }
                    }, 1500);
                }
            });

            alt1Button.setText(wrongLetterAlternative);
            alt1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_incorrect);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                    alt1Button.startAnimation(animation);
                    alt1Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playLetterSound(letter);

                            rippleBackground.startRippleAnimation();
                            rippleBackground.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rippleBackground.stopRippleAnimation();
                                }
                            }, 600);
                        }
                    }, 1000);
                }
            });
        }

//        MediaPlayerHelper.play(getApplicationContext(), R.raw.activity_instruction_letter_identification);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.activity_instruction_letter_identification);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(getClass().getName(), "mediaPlayer onCompletion");
                mediaPlayer.release();

                playLetterSound(letter);

                rippleBackground.startRippleAnimation();
                rippleBackground.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBackground.stopRippleAnimation();
                    }
                }, 600);
            }
        });
        mediaPlayer.start();

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "listenButton onClick");

                playLetterSound(letter);

                rippleBackground.startRippleAnimation();
                rippleBackground.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBackground.stopRippleAnimation();
                    }
                }, 600);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getName(), "onClick");

                Intent intent = new Intent(getApplicationContext(), TypeLetterActivity.class);
                intent.putExtra("letter", letter.getText());
                startActivity(intent);

                finish();
            }
        });
    }

    private void playLetterSound(Letter letter) {
        Log.i(getClass().getName(), "playLetterSound");

        // Look up corresponding Audio
        Log.d(getClass().getName(), "Looking up \"letter_sound_" + letter.getText() + "\"");
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq("letter_sound_" + letter.getText()))
                .unique();
        Log.i(getClass().getName(), "audio: " + audio);
        if (audio != null) {
            // Play audio
            File audioFile = MultimediaHelper.getFile(audio);
            Uri uri = Uri.parse(audioFile.getAbsolutePath());
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.i(getClass().getName(), "mediaPlayer onCompletion");
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } else {
            // Audio not found. Fall-back to application resource.
            String audioFileName = "letter_sound_" + letter.getText();
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
        }
    }
}
