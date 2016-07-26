package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.VowelHeight;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class VowelHeightConverter implements PropertyConverter<VowelHeight, String> {

    @Override
    public VowelHeight convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        VowelHeight entityProperty = VowelHeight.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(VowelHeight entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
