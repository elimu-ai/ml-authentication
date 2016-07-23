package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.Locale;
import org.literacyapp.util.Log;
import de.greenrobot.dao.converter.PropertyConverter;

public class LocaleConverter implements PropertyConverter<Locale, String> {

    @Override
    public Locale convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        Locale locale = Locale.valueOf(databaseValue);
        Log.d(getClass(), "locale: " + locale);
        return locale;
    }

    @Override
    public String convertToDatabaseValue(Locale entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
