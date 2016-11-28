package org.literacyapp.dao;

import org.literacyapp.model.content.Allophone;
import org.literacyapp.model.content.multimedia.Audio;
import org.literacyapp.model.content.multimedia.Image;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.content.Number;
import org.literacyapp.model.content.multimedia.Video;
import org.literacyapp.model.content.Word;
import org.literacyapp.model.gson.content.AllophoneGson;
import org.literacyapp.model.gson.content.LetterGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;
import org.literacyapp.model.gson.content.multimedia.AudioGson;
import org.literacyapp.model.gson.content.multimedia.ImageGson;
import org.literacyapp.model.gson.content.multimedia.VideoGson;

public class GsonToGreenDaoConverter {

    public static Allophone getAllophone(AllophoneGson allophoneGson) {
        if (allophoneGson == null) {
            return null;
        } else {
            Allophone allophone = new Allophone();

            allophone.setId(allophoneGson.getId());

            allophone.setLocale(allophoneGson.getLocale());
            allophone.setTimeLastUpdate(allophoneGson.getTimeLastUpdate());
            allophone.setRevisionNumber(allophoneGson.getRevisionNumber());
            allophone.setContentStatus(allophoneGson.getContentStatus());

            allophone.setValueIpa(allophoneGson.getValueIpa());
            allophone.setValueSampa(allophoneGson.getValueSampa());
            allophone.setSoundType(allophoneGson.getSoundType());
            allophone.setVowelLength(allophoneGson.getVowelLength());
            allophone.setVowelHeight(allophoneGson.getVowelHeight());
            allophone.setVowelFrontness(allophoneGson.getVowelFrontness());
            allophone.setLipRounding(allophoneGson.getLipRounding());
            allophone.setConsonantType(allophoneGson.getConsonantType());
            allophone.setConsonantPlace(allophoneGson.getConsonantPlace());
            allophone.setConsonantVoicing(allophoneGson.getConsonantVoicing());

            return allophone;
        }
    }

    public static Letter getLetter(LetterGson letterGson) {
        if (letterGson == null) {
            return null;
        } else {
            Letter letter = new Letter();

            letter.setId(letterGson.getId());

            letter.setLocale(letterGson.getLocale());
            letter.setTimeLastUpdate(letterGson.getTimeLastUpdate());
            letter.setRevisionNumber(letterGson.getRevisionNumber());
            letter.setContentStatus(letterGson.getContentStatus());

            letter.setText(letterGson.getText());

            return letter;
        }
    }

    public static Number getNumber(NumberGson numberGson) {
        if (numberGson == null) {
            return null;
        } else {
            Number number = new Number();

            number.setId(numberGson.getId());

            number.setLocale(numberGson.getLocale());
            number.setTimeLastUpdate(numberGson.getTimeLastUpdate());
            number.setRevisionNumber(numberGson.getRevisionNumber());
            number.setContentStatus(numberGson.getContentStatus());

            number.setValue(numberGson.getValue());
            number.setSymbol(numberGson.getSymbol());
            number.setWord(getWord(numberGson.getWord()));

            return number;
        }
    }

    public static Word getWord(WordGson wordGson) {
        if (wordGson == null) {
            return null;
        } else {
            Word word = new Word();

            word.setId(wordGson.getId());

            word.setLocale(wordGson.getLocale());
            word.setTimeLastUpdate(wordGson.getTimeLastUpdate());
            word.setRevisionNumber(wordGson.getRevisionNumber());
            word.setContentStatus(wordGson.getContentStatus());

            word.setText(wordGson.getText());

            return word;
        }
    }

    public static Audio getAudio(AudioGson audioGson) {
        if (audioGson == null) {
            return null;
        } else {
            Audio audio = new Audio();

            audio.setId(audioGson.getId());

            audio.setLocale(audioGson.getLocale());
            audio.setTimeLastUpdate(audioGson.getTimeLastUpdate());
            audio.setRevisionNumber(audioGson.getRevisionNumber());
            audio.setContentStatus(audioGson.getContentStatus());

            audio.setContentType(audioGson.getContentType());
            audio.setLiteracySkills(audioGson.getLiteracySkills());
            audio.setNumeracySkills(audioGson.getNumeracySkills());

            audio.setTranscription(audioGson.getTranscription());
            audio.setAudioFormat(audioGson.getAudioFormat());

            return audio;
        }
    }

    public static Image getImage(ImageGson imageGson) {
        if (imageGson == null) {
            return null;
        } else {
            Image image = new Image();

            image.setId(imageGson.getId());

            image.setLocale(imageGson.getLocale());
            image.setTimeLastUpdate(imageGson.getTimeLastUpdate());
            image.setRevisionNumber(imageGson.getRevisionNumber());
            image.setContentStatus(imageGson.getContentStatus());

            image.setContentType(imageGson.getContentType());
            image.setLiteracySkills(imageGson.getLiteracySkills());
            image.setNumeracySkills(imageGson.getNumeracySkills());

            image.setTitle(imageGson.getTitle());
            image.setImageFormat(imageGson.getImageFormat());
            image.setDominantColor(imageGson.getDominantColor());

            return image;
        }
    }

    public static Video getVideo(VideoGson videoGson) {
        if (videoGson == null) {
            return null;
        } else {
            Video video = new Video();

            video.setId(videoGson.getId());

            video.setLocale(videoGson.getLocale());
            video.setTimeLastUpdate(videoGson.getTimeLastUpdate());
            video.setRevisionNumber(videoGson.getRevisionNumber());
            video.setContentStatus(videoGson.getContentStatus());

            video.setContentType(videoGson.getContentType());
            video.setLiteracySkills(videoGson.getLiteracySkills());
            video.setNumeracySkills(videoGson.getNumeracySkills());

            video.setTitle(videoGson.getTitle());
            video.setVideoFormat(videoGson.getVideoFormat());

            return video;
        }
    }
}
