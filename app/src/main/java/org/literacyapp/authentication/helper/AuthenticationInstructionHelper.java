package org.literacyapp.authentication.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.ImageView;

import org.literacyapp.R;
import org.literacyapp.contentprovider.model.content.multimedia.Audio;
import org.literacyapp.contentprovider.model.content.multimedia.Image;
import org.literacyapp.contentprovider.model.content.multimedia.Video;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class for fetching image overlay displayed during authentication.
 */
public class AuthenticationInstructionHelper {

    private static final String AUTHENTICATION_INSTRUCTION_BOY = "authentication_instruction_640";
    private static final String AUTHENTICATION_INSTRUCTION_GIRL = "authentication_instruction_girl_640";
    public static final String RESOURCES_DRAWABLE_FOLDER = "drawable";
    public static final String RESOURCES_RAW_FOLDER = "raw";

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static void setAuthenticationInstructionAnimation(Context context, ImageView animationImageView){
        int randomIndex = (int) (Math.random() * 2);
        switch (randomIndex){
            case 0:
                animationImageView.setImageResource(context.getResources().getIdentifier(AUTHENTICATION_INSTRUCTION_BOY, RESOURCES_DRAWABLE_FOLDER, context.getPackageName()));
                break;
            case 1:
                animationImageView.setImageResource(context.getResources().getIdentifier(AUTHENTICATION_INSTRUCTION_GIRL, RESOURCES_DRAWABLE_FOLDER, context.getPackageName()));
                break;
            default:
                animationImageView.setImageResource(context.getResources().getIdentifier(AUTHENTICATION_INSTRUCTION_BOY, RESOURCES_DRAWABLE_FOLDER, context.getPackageName()));
                break;
        }
    }

    public static MediaPlayer getMediaPlayerTabletPlacement(Context context){
        return MediaPlayer.create(context, R.raw.auth_tablet_placement);
    }

    public static MediaPlayer getMediaPlayerTabletPlacementOverlay(Context context){
        return MediaPlayer.create(context, R.raw.auth_tablet_placement_overlay);
    }

    public static synchronized void playTabletPlacementOverlay(MediaPlayer mediaPlayerTabletPlacement, MediaPlayer mediaPlayerTabletPlacementOverlay, MediaPlayer mediaPlayerAnimalSound){
        if (mediaPlayerTabletPlacement != null){
            if ((!mediaPlayerTabletPlacement.isPlaying()) && (!mediaPlayerTabletPlacementOverlay.isPlaying()) && (!mediaPlayerAnimalSound.isPlaying())){
                mediaPlayerTabletPlacementOverlay.start();
            }
        } else {
            if ((!mediaPlayerTabletPlacementOverlay.isPlaying()) && (!mediaPlayerAnimalSound.isPlaying())){
                mediaPlayerTabletPlacementOverlay.start();
            }
        }
    }
}
