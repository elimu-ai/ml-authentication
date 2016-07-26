package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.VowelFrontness;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class VowelFrontnessConverter implements PropertyConverter<VowelFrontness, String> {

    @Override
    public VowelFrontness convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        VowelFrontness entityProperty = VowelFrontness.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(VowelFrontness entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
