package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.ContentStatus;

import de.greenrobot.dao.converter.PropertyConverter;

public class ContentStatusConverter implements PropertyConverter<ContentStatus, String> {

    @Override
    public ContentStatus convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        ContentStatus entityProperty = ContentStatus.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ContentStatus entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
