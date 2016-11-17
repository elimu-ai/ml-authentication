package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.Locale;

import de.greenrobot.dao.converter.PropertyConverter;

public class LocaleConverter implements PropertyConverter<Locale, String> {

    @Override
    public Locale convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        Locale locale = Locale.valueOf(databaseValue);
        Log.i(getClass().getName(), "locale: " + locale);
        return locale;
    }

    @Override
    public String convertToDatabaseValue(Locale entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
