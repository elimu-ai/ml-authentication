package org.literacyapp.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Utility class which helps releasing the {@link android.media.MediaPlayer} instance after
 * finishing playing the audio.
 * <p />
 *
 * See https://developer.android.com/reference/android/media/MediaPlayer.html#create%28android.content.Context,%20int%29
 */
public class MediaPlayerHelper {

    public static void play(Context context, int resId) {
        Log.i(MediaPlayerHelper.class.getName(), "play");

        final MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }
}
