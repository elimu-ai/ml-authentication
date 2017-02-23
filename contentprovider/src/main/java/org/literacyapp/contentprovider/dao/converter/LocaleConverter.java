package org.literacyapp.contentprovider.dao.converter;

import android.util.Log;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.literacyapp.model.enums.Locale;

public class LocaleConverter implements PropertyConverter<Locale, String> {

    @Override
    public Locale convertToEntityProperty(String databaseValue) {
        Log.d(getClass().getName(), "convertToEntityProperty");

        Locale locale = Locale.valueOf(databaseValue);
        Log.d(getClass().getName(), "locale: " + locale);
        return locale;
    }

    @Override
    public String convertToDatabaseValue(Locale entityProperty) {
        Log.d(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
