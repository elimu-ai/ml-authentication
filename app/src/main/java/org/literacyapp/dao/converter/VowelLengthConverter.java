package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.VowelLength;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class VowelLengthConverter implements PropertyConverter<VowelLength, String> {

    @Override
    public VowelLength convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        VowelLength entityProperty = VowelLength.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(VowelLength entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
