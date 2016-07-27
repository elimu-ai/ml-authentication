package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.VideoFormat;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class VideoFormatConverter implements PropertyConverter<VideoFormat, String> {

    @Override
    public VideoFormat convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        VideoFormat entityProperty = VideoFormat.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(VideoFormat entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
