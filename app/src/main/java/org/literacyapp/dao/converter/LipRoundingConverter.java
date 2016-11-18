package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.LipRounding;

import de.greenrobot.dao.converter.PropertyConverter;

public class LipRoundingConverter implements PropertyConverter<LipRounding, String> {

    @Override
    public LipRounding convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        LipRounding entityProperty = LipRounding.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(LipRounding entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
