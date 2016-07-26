package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.ConsonantVoicing;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantVoicingConverter implements PropertyConverter<ConsonantVoicing, String> {

    @Override
    public ConsonantVoicing convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ConsonantVoicing entityProperty = ConsonantVoicing.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantVoicing entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
