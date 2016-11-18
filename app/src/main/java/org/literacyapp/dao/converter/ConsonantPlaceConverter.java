package org.literacyapp.dao.converter;

import android.util.Log;

import org.literacyapp.model.enums.content.allophone.ConsonantPlace;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantPlaceConverter implements PropertyConverter<ConsonantPlace, String> {

    @Override
    public ConsonantPlace convertToEntityProperty(String databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        ConsonantPlace entityProperty = ConsonantPlace.valueOf(databaseValue);
        Log.i(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantPlace entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
