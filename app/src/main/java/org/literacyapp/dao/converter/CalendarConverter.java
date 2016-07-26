package org.literacyapp.dao.converter;

import org.literacyapp.util.Log;

import java.util.Calendar;

import de.greenrobot.dao.converter.PropertyConverter;

public class CalendarConverter implements PropertyConverter<Calendar, Long> {

    @Override
    public Calendar convertToEntityProperty(Long databaseValue) {
        Log.d(getClass(), "convertToEntityProperty");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(databaseValue);
        Log.d(getClass(), "calendar.getTime(): " + calendar.getTime());
        return calendar;
    }

    @Override
    public Long convertToDatabaseValue(Calendar entityProperty) {
        Log.d(getClass(), "convertToDatabaseValue");

        Long databaseValue = entityProperty.getTimeInMillis();
        Log.d(getClass(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
