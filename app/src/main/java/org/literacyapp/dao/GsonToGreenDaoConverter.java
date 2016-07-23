package org.literacyapp.dao;

import org.literacyapp.model.gson.content.NumberGson;
import org.literacyapp.model.gson.content.WordGson;

public class GsonToGreenDaoConverter {

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
//            number.setDominantColor(); // TODO
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
