package org.literacyapp.util;

import android.os.Environment;

import org.literacyapp.dao.Audio;
import org.literacyapp.dao.Image;
import org.literacyapp.dao.Video;

import java.io.File;

public class MultimediaHelper {

    public static File getMultimediaDirectory() {
        File multimediaDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "LiteracyApp" + File.separator + "multimedia");
        if (!multimediaDirectory.exists()) {
            multimediaDirectory.mkdirs();
        }
        return multimediaDirectory;
    }

    public static File getAudioDirectory() {
        File audioDirectory = new File(getMultimediaDirectory(), "audio");
        if (!audioDirectory.exists()) {
            audioDirectory.mkdir();
        }
        return audioDirectory;
    }

    public static File getImageDirectory() {
        File imageDirectory = new File(getMultimediaDirectory(), "image");
        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }
        return imageDirectory;
    }

    public static File getVideoDirectory() {
        File videoDirectory = new File(getMultimediaDirectory(), "video");
        if (!videoDirectory.exists()) {
            videoDirectory.mkdir();
        }
        return videoDirectory;
    }

    public static File getFile(Audio audio) {
        File file = null;

        File audioDirectory = getAudioDirectory();
        file = new File(audioDirectory, audio.getId() + "_r" + audio.getRevisionNumber() + "." + audio.getAudioFormat().toString().toLowerCase());

        return file;
    }

    public static File getFile(Image image) {
        File file = null;

        File imageDirectory = getAudioDirectory();
        file = new File(imageDirectory, image.getId() + "_r" + image.getRevisionNumber() + "." + image.getImageFormat().toString().toLowerCase());

        return file;
    }

    public static File getFile(Video video) {
        File file = null;

        File videoDirectory = getAudioDirectory();
        file = new File(videoDirectory, video.getId() + "_r" + video.getRevisionNumber() + "." + video.getVideoFormat().toString().toLowerCase());

        return file;
    }
}
