package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.SoundType;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class SoundTypeConverter implements PropertyConverter<SoundType, String> {

    @Override
    public SoundType convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        SoundType entityProperty = SoundType.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(SoundType entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
