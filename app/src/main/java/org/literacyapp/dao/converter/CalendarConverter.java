package org.literacyapp.dao.converter;

import android.util.Log;

import java.util.Calendar;

import de.greenrobot.dao.converter.PropertyConverter;

public class CalendarConverter implements PropertyConverter<Calendar, Long> {

    @Override
    public Calendar convertToEntityProperty(Long databaseValue) {
        Log.i(getClass().getName(), "convertToEntityProperty");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(databaseValue);
        Log.i(getClass().getName(), "calendar.getTime(): " + calendar.getTime());
        return calendar;
    }

    @Override
    public Long convertToDatabaseValue(Calendar entityProperty) {
        Log.i(getClass().getName(), "convertToDatabaseValue");

        Long databaseValue = entityProperty.getTimeInMillis();
        Log.i(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
