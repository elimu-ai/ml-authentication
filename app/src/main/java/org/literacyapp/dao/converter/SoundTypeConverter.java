package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.SoundType;

import de.greenrobot.dao.converter.PropertyConverter;

public class SoundTypeConverter implements PropertyConverter<SoundType, String> {

    @Override
    public SoundType convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        SoundType entityProperty = SoundType.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(SoundType entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
