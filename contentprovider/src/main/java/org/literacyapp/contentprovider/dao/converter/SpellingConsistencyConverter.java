package org.literacyapp.contentprovider.dao.converter;

import android.util.Log;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.literacyapp.model.enums.content.SpellingConsistency;

public class SpellingConsistencyConverter implements PropertyConverter<SpellingConsistency, String> {

    @Override
    public SpellingConsistency convertToEntityProperty(String databaseValue) {
        Log.d(getClass().getName(), "convertToEntityProperty");

        SpellingConsistency entityProperty = SpellingConsistency.valueOf(databaseValue);
        Log.d(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(SpellingConsistency entityProperty) {
        Log.d(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
