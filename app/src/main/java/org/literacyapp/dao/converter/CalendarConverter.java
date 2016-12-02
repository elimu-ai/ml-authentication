package org.literacyapp.dao.converter;

import android.util.Log;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Calendar;

public class CalendarConverter implements PropertyConverter<Calendar, Long> {

    @Override
    public Calendar convertToEntityProperty(Long databaseValue) {
        Log.d(getClass().getName(), "convertToEntityProperty");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(databaseValue);
        Log.d(getClass().getName(), "calendar.getTime(): " + calendar.getTime());
        return calendar;
    }

    @Override
    public Long convertToDatabaseValue(Calendar entityProperty) {
        Log.d(getClass().getName(), "convertToDatabaseValue");

        Long databaseValue = entityProperty.getTimeInMillis();
        Log.d(getClass().getName(), "databaseValue: " + databaseValue);
        return databaseValue;
    }
}
