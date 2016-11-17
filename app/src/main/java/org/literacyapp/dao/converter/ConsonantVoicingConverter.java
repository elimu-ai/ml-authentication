package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.ConsonantVoicing;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantVoicingConverter implements PropertyConverter<ConsonantVoicing, String> {

    @Override
    public ConsonantVoicing convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        ConsonantVoicing entityProperty = ConsonantVoicing.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantVoicing entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
