package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.ImageType;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ImageTypeConverter implements PropertyConverter<ImageType, String> {

    @Override
    public ImageType convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ImageType entityProperty = ImageType.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ImageType entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
