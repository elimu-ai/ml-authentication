package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.AudioFormat;

import de.greenrobot.dao.converter.PropertyConverter;

public class AudioFormatConverter implements PropertyConverter<AudioFormat, String> {

    @Override
    public AudioFormat convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        AudioFormat entityProperty = AudioFormat.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(AudioFormat entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
