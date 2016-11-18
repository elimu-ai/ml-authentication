package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.VowelHeight;

import de.greenrobot.dao.converter.PropertyConverter;

public class VowelHeightConverter implements PropertyConverter<VowelHeight, String> {

    @Override
    public VowelHeight convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        VowelHeight entityProperty = VowelHeight.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(VowelHeight entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
