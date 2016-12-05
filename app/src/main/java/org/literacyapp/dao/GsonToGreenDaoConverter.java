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

import java.util.ArrayList;
import java.util.List;

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
            word.setPhonetics(wordGson.getPhonetics());
            word.setUsageCount(wordGson.getUsageCount());

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

            if (audioGson.getLetters() != null) {
                List<Letter> letters = new ArrayList<>();
                for (LetterGson letterGson : audioGson.getLetters()) {
                    Letter letter = getLetter(letterGson);
                    letters.add(letter);
                }
                audio.setLetters(letters);
            }

            if (audioGson.getNumbers() != null) {
                List<Number> numbers = new ArrayList<>();
                for (NumberGson numberGson : audioGson.getNumbers()) {
                    Number number = getNumber(numberGson);
                    numbers.add(number);
                }
                audio.setNumbers(numbers);
            }

            if (audioGson.getWords() != null) {
                List<Word> words = new ArrayList<>();
                for (WordGson wordGson : audioGson.getWords()) {
                    Word word = getWord(wordGson);
                    words.add(word);
                }
                audio.setWords(words);
            }

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

            if (imageGson.getLetters() != null) {
                List<Letter> letters = new ArrayList<>();
                for (LetterGson letterGson : imageGson.getLetters()) {
                    Letter letter = getLetter(letterGson);
                    letters.add(letter);
                }
                image.setLetters(letters);
            }

            if (imageGson.getNumbers() != null) {
                List<Number> numbers = new ArrayList<>();
                for (NumberGson numberGson : imageGson.getNumbers()) {
                    Number number = getNumber(numberGson);
                    numbers.add(number);
                }
                image.setNumbers(numbers);
            }

            if (imageGson.getWords() != null) {
                List<Word> words = new ArrayList<>();
                for (WordGson wordGson : imageGson.getWords()) {
                    Word word = getWord(wordGson);
                    words.add(word);
                }
                image.setWords(words);
            }

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

            if (videoGson.getLetters() != null) {
                List<Letter> letters = new ArrayList<>();
                for (LetterGson letterGson : videoGson.getLetters()) {
                    Letter letter = getLetter(letterGson);
                    letters.add(letter);
                }
                video.setLetters(letters);
            }

            if (videoGson.getNumbers() != null) {
                List<Number> numbers = new ArrayList<>();
                for (NumberGson numberGson : videoGson.getNumbers()) {
                    Number number = getNumber(numberGson);
                    numbers.add(number);
                }
                video.setNumbers(numbers);
            }

            if (videoGson.getWords() != null) {
                List<Word> words = new ArrayList<>();
                for (WordGson wordGson : videoGson.getWords()) {
                    Word word = getWord(wordGson);
                    words.add(word);
                }
                video.setWords(words);
            }

            video.setTitle(videoGson.getTitle());
            video.setVideoFormat(videoGson.getVideoFormat());

            return video;
        }
    }
}
