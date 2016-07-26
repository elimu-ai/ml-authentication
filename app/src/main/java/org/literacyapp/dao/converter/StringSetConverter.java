package org.literacyapp.dao.converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.literacyapp.util.Log;

import java.util.HashSet;
import java.util.Set;

import de.greenrobot.dao.converter.PropertyConverter;

public class StringSetConverter implements PropertyConverter<Set, String> {

    @Override
    public Set convertToEntityProperty(String databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        Set<String> set = new HashSet<>();

        try {
            JSONArray jsonArray = new JSONArray(databaseValue);
            Log.d(getClass(), "jsonArray: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                Log.d(getClass(), "value: " + value);
                set.add(value);
            }
        } catch (JSONException e) {
            Log.e(getClass(), null, e);
        }

        return set;
    }

    @Override
    public String convertToDatabaseValue(Set entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
