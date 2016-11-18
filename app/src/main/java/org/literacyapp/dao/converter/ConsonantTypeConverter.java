package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.ConsonantType;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantTypeConverter implements PropertyConverter<ConsonantType, String> {

    @Override
    public ConsonantType convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        ConsonantType entityProperty = ConsonantType.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantType entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
