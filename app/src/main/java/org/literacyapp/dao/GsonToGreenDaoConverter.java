package org.literacyapp.dao;

import org.literacyapp.model.gson.content.AllophoneGson;
import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;

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

    public static Number getNumber(NumberGson numberGson) {
        if (numberGson == null) {
            return null;
        } else {
            Number number = new Number();
            number.setId(numberGson.getId());
            number.setLocale(numberGson.getLocale());
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
            word.setText(wordGson.getText());
            return word;
        }
    }
}
