package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.ContentStatus;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ContentStatusConverter implements PropertyConverter<ContentStatus, String> {

    @Override
    public ContentStatus convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ContentStatus entityProperty = ContentStatus.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ContentStatus entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
