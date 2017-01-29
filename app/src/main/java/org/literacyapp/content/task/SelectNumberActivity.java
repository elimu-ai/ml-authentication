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
import android.widget.Button;
import android.widget.ImageButton;

import com.skyfishjy.library.RippleBackground;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.R;
import org.literacyapp.dao.AudioDao;
import org.literacyapp.dao.JoinVideosWithNumbersDao;
import org.literacyapp.dao.NumberDao;
import org.literacyapp.dao.VideoDao;
import org.literacyapp.logic.CurriculumHelper;
import org.literacyapp.model.Student;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.model.content.multimedia.JoinVideosWithNumbers;
import org.literacyapp.model.content.multimedia.Video;
import org.literacyapp.util.MediaPlayerHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.TtsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectNumberActivity extends AppCompatActivity {

    private RippleBackground rippleBackground;
    private Button listenButton;
    private Button alt1Button;
    private Button alt2Button;

    private ImageButton nextButton;

    private NumberDao numberDao;
    private AudioDao audioDao;
    private VideoDao videoDao;
    private JoinVideosWithNumbersDao joinVideosWithNumbersDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_number);

        rippleBackground = (RippleBackground) findViewById(R.id.rippleBackground);
        listenButton = (Button) findViewById(R.id.listenButton);
        alt1Button = (Button) findViewById(R.id.alt1Button);
        alt2Button = (Button) findViewById(R.id.alt2Button);

        nextButton = (ImageButton) findViewById(R.id.nextButton);

        LiteracyApplication literacyApplication = (LiteracyApplication) getApplicationContext();
        numberDao = literacyApplication.getDaoSession().getNumberDao();
        audioDao = literacyApplication.getDaoSession().getAudioDao();
        videoDao = literacyApplication.getDaoSession().getVideoDao();
        joinVideosWithNumbersDao = literacyApplication.getDaoSession().getJoinVideosWithNumbersDao();
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

        Student student = null;
        // TODO: get current student
        List<Number> availableNumbers = new CurriculumHelper(getApplicationContext()).getAvailableNumbers(student);
        ArrayList<Integer> availableNumbersArrayList = new ArrayList<>();
        for (Number availableNumber : availableNumbers) {
            availableNumbersArrayList.add(availableNumber.getValue());
        }
        Log.i(getClass().getName(), "availableNumbersArrayList: " + availableNumbersArrayList);
        availableNumbersArrayList.remove(number.getValue());
        Log.i(getClass().getName(), "availableNumbersArrayList (after removing current number): " + availableNumbersArrayList);
        int randomIndex = (int) (Math.random() * availableNumbersArrayList.size());
        int wrongNumberAlternative = availableNumbersArrayList.get(randomIndex);
        Log.i(getClass().getName(), "wrongNumberAlternative: " + wrongNumberAlternative);
        boolean isAlt1Correct = (Math.random() > .5);
        if (isAlt1Correct) {
            alt1Button.setText(String.valueOf(number.getValue()));
            alt1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alt1Button.setTextColor(Color.WHITE);
                    alt1Button.setBackgroundColor(Color.parseColor("#009688"));
                    alt2Button.setEnabled(false);
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_correct);
                    alt1Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playNumberSound(number);

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

            alt2Button.setText(String.valueOf(wrongNumberAlternative));
            alt2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_incorrect);
                    alt2Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playNumberSound(number);

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
            alt2Button.setText(String.valueOf(number.getValue()));
            alt2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alt2Button.setTextColor(Color.WHITE);
                    alt2Button.setBackgroundColor(Color.parseColor("#009688"));
                    alt1Button.setEnabled(false);
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_correct);
                    alt2Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playNumberSound(number);

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

            alt1Button.setText(String.valueOf(wrongNumberAlternative));
            alt1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerHelper.play(getApplicationContext(), R.raw.alternative_incorrect);
                    alt1Button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playNumberSound(number);

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

//        MediaPlayerHelper.play(getApplicationContext(), R.raw.activity_instruction_number_identification);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.activity_instruction_number_identification);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(getClass().getName(), "mediaPlayer onCompletion");
                mediaPlayer.release();

                playNumberSound(number);

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

                playNumberSound(number);

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

                // Look up video(s) containing number
                List<Video> videosContainingNumber = new ArrayList<Video>();
                List<JoinVideosWithNumbers> joinVideosWithNumbersList = joinVideosWithNumbersDao.queryBuilder()
                        .where(JoinVideosWithNumbersDao.Properties.NumberId.eq(number.getId()))
                        .list();
                Log.d(getClass().getName(), "joinVideosWithNumbersList.size(): " + joinVideosWithNumbersList.size());
                if (!joinVideosWithNumbersList.isEmpty()) {
                    for (JoinVideosWithNumbers joinVideosWithNumbers : joinVideosWithNumbersList) {
                        Video video = videoDao.load(joinVideosWithNumbers.getVideoId());
                        Log.d(getClass().getName(), "Adding video with id " + video.getId());
                        videosContainingNumber.add(video);
                    }
                }
                Log.d(getClass().getName(), "videosContainingNumber.size(): " + videosContainingNumber.size());
                if (!videosContainingNumber.isEmpty()) {
                    // Redirect to video(s)
                    Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                    int randomIndex = (int) (Math.random() * videosContainingNumber.size());
                    Video video = videosContainingNumber.get(randomIndex); // TODO: iterate all videos
                    intent.putExtra(VideoActivity.EXTRA_KEY_VIDEO_ID, video.getId());
                    startActivity(intent);
                } else {
                    Intent loadingIntent = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(loadingIntent);
                }

                finish();
            }
        });
    }

    private void playNumberSound(Number number) {
        Log.i(getClass().getName(), "playNumberSound");

        // Look up corresponding Audio
        Log.d(getClass().getName(), "Looking up \"digit_" + number.getValue() + "\"");
        final Audio audio = audioDao.queryBuilder()
                .where(AudioDao.Properties.Transcription.eq("digit_" + number.getValue()))
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
            String audioFileName = "digit_" + number.getValue();
            int resourceId = getResources().getIdentifier(audioFileName, "raw", getPackageName());
            try {
                if (resourceId != 0) {
                    MediaPlayerHelper.play(getApplicationContext(), resourceId);
                } else {
                    // Fall-back to TTS
                    TtsHelper.speak(getApplicationContext(), number.getWord().getText());
                }
            } catch (Resources.NotFoundException e) {
                // Fall-back to TTS
                TtsHelper.speak(getApplicationContext(), number.getWord().getText());
            }
        }
    }
}
