package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.AudioFormat;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class AudioFormatConverter implements PropertyConverter<AudioFormat, String> {

    @Override
    public AudioFormat convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        AudioFormat entityProperty = AudioFormat.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(AudioFormat entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
