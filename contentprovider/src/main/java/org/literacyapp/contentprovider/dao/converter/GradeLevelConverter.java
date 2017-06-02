package org.literacyapp.contentprovider.dao.converter;

import android.util.Log;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.literacyapp.model.enums.GradeLevel;
import org.literacyapp.model.enums.Locale;

public class GradeLevelConverter implements PropertyConverter<GradeLevel, String> {

    @Override
    public GradeLevel convertToEntityProperty(String databaseValue) {
        Log.d(getClass().getName(), "convertToEntityProperty");

        GradeLevel gradeLevel = GradeLevel.valueOf(databaseValue);
        Log.d(getClass().getName(), "gradeLevel: " + gradeLevel);
        return gradeLevel;
    }

    @Override
    public String convertToDatabaseValue(GradeLevel entityProperty) {
        Log.d(getClass().getName(), "convertToDatabaseValue");

        String databaseValue = entityProperty.toString();
        Log.d(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
