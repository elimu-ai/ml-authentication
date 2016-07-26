package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.ConsonantType;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantTypeConverter implements PropertyConverter<ConsonantType, String> {

    @Override
    public ConsonantType convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ConsonantType entityProperty = ConsonantType.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantType entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
