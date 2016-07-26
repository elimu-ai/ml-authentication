package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.LipRounding;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class LipRoundingConverter implements PropertyConverter<LipRounding, String> {

    @Override
    public LipRounding convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        LipRounding entityProperty = LipRounding.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(LipRounding entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
