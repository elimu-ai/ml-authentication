package org.literacyapp.dao.converter;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.literacyapp.model.enums.Language;
import org.literacyapp.util.Log;

public class LanguageConverter implements PropertyConverter<Language, String> {

    @Override
    public Language convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        Language language = Language.valueOf(databaseValue);
        Log.d(getClass(), "language: " + language);
        return language;
    }

    @Override
    public String convertToDatabaseValue(Language entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
