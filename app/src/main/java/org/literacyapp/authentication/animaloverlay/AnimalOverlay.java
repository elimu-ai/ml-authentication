package org.literacyapp.authentication.animaloverlay;

import java.io.File;

/**
 * Created by sladomic on 27.12.16.
 */

public class AnimalOverlay {
    private String name;
    private File animalTemplateFile;
    private int imageWidth;
    private int imageHeight;
    private int frameStartX;
    private int frameStartY;
    private int frameEndX;
    private int frameEndY;
    private String soundFile;

    public AnimalOverlay(String name, File animalTemplateFile, int imageWidth, int imageHeight, int frameStartX, int frameStartY, int frameEndX, int frameEndY, String soundFile) {
        this.name = name;
        this.animalTemplateFile = animalTemplateFile;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
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

    public File getAnimalTemplateFile() {
        return animalTemplateFile;
    }

    public void setAnimalTemplateFile(File animalTemplateFile) {
        this.animalTemplateFile = animalTemplateFile;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
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
