package org.literacyapp.contentprovider.dao.converter;

import android.util.Log;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.literacyapp.model.enums.content.allophone.ConsonantVoicing;

public class ConsonantVoicingConverter implements PropertyConverter<ConsonantVoicing, String> {

    @Override
    public ConsonantVoicing convertToEntityProperty(String databaseValue) {
        Log.d(getClass().getName(), "convertToEntityProperty");

        ConsonantVoicing entityProperty = ConsonantVoicing.valueOf(databaseValue);
        Log.d(getClass().getName(), "entityProperty: " + entityProperty);
        return entityProperty;
    }

    @Override
    public String convertToDatabaseValue(ConsonantVoicing entityProperty) {
        Log.d(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
