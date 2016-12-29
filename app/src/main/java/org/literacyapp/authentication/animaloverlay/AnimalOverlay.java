package org.literacyapp.authentication.animaloverlay;

import java.io.File;

/**
 * Created by sladomic on 27.12.16.
 */

public class AnimalOverlay {
    private String name;
    private int frameStartX;
    private int frameStartY;
    private int frameEndX;
    private int frameEndY;
    private String soundFile;

    public AnimalOverlay(String name, int frameStartX, int frameStartY, int frameEndX, int frameEndY, String soundFile) {
        this.name = name;
        this.frameStartX = frameStartX;
        this.frameStartY = frameStartY;
        this.frameEndX = frameEndX;
        this.frameEndY = frameEndY;
        this.soundFile = soundFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrameStartX() {
        return frameStartX;
    }

    public void setFrameStartX(int frameStartX) {
        this.frameStartX = frameStartX;
    }

    public int getFrameStartY() {
        return frameStartY;
    }

    public void setFrameStartY(int frameStartY) {
        this.frameStartY = frameStartY;
    }

    public int getFrameEndX() {
        return frameEndX;
    }

    public void setFrameEndX(int frameEndX) {
        this.frameEndX = frameEndX;
    }

    public int getFrameEndY() {
        return frameEndY;
    }

    public void setFrameEndY(int frameEndY) {
        this.frameEndY = frameEndY;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }
}
