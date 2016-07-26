package org.literacyapp.dao.converter;

import org.literacyapp.model.enums.content.allophone.ConsonantPlace;
import org.literacyapp.util.Log;

import de.greenrobot.dao.converter.PropertyConverter;

public class ConsonantPlaceConverter implements PropertyConverter<ConsonantPlace, String> {

    @Override
    public ConsonantPlace convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        ConsonantPlace entityProperty = ConsonantPlace.valueOf(databaseValue);
        Log.d(getClass(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantPlace entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
