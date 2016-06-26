package org.literacyapp.dao;

import org.literacyapp.model.json.NumberJson;
import org.literacyapp.model.json.WordJson;

public class JsonToGreenDaoConverter {

    public static Number getNumber(NumberJson numberJson) {
        if (numberJson == null) {
            return null;
        } else {
            Number number = new Number();
            number.setId(numberJson.getId());
            number.setLocale(numberJson.getLocale());
            number.setValue(numberJson.getValue());
            number.setSymbol(numberJson.getSymbol());
            number.setWord(getWord(numberJson.getWord()));
//            number.setDominantColor(); // TODO
            return number;
        }
    }

    public static Word getWord(WordJson wordJson) {
        if (wordJson == null) {
            return null;
        } else {
            Word word = new Word();
            word.setId(wordJson.getId());
            word.setLocale(wordJson.getLocale());
            word.setText(wordJson.getText());
            return word;
        }
    }
}
