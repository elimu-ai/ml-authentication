package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.ImageFormat;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ImageFormatConverter implements PropertyConverter<ImageFormat, String> {

    @Override
    public ImageFormat convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ImageFormat entityProperty = ImageFormat.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ImageFormat entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
