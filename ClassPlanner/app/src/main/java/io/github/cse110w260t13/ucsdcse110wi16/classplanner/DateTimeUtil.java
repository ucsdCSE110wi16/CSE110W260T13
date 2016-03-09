package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

/**
 * Utility Class to convert dates and times from String to int or int to String
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateTimeUtil {

    public static final int HOUR = 0;
    public static final int MINUTE = 1;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;

    private DateTimeUtil(){}

    public static int getDateFromDate(Date date, int field){
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        switch(field){
            case YEAR:
                return cal.get(Calendar.YEAR);
            case MONTH:
                return cal.get(Calendar.MONTH);
            case DAY:
                return cal.get(Calendar.DAY_OF_MONTH);
        }
        return -1;
    }

    public static int getDateFromString(String strDate, int field)
    {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date convertedDate = new Date();
        try {
            convertedDate = dayFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertedDate);

        switch(field){
            case YEAR:
                return cal.get(Calendar.YEAR);
            case MONTH:
                return cal.get(Calendar.MONTH);
            case DAY:
                return cal.get(Calendar.DAY_OF_MONTH);
        }
        return -1;
    }

    public static int getTime(String time, int field)
    {
        String[] splitTime = time.split(":");
        if(splitTime.length == 2) {
            if (!splitTime[0].isEmpty() && !splitTime[1].isEmpty()) {
                if (field == HOUR) {
                    return Integer.parseInt(splitTime[0]);
                } else if (field == MINUTE) {
                    return Integer.parseInt(splitTime[1]);
                }
            }
        }
        else{
            if (field == HOUR){
                return 12;
            }
            else if (field == MINUTE){
                return 0;
            }
        }
        return -1;
    }

    public static String getDateString(int year, int month, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        Date date = cal.getTime();

        return sdf.format(date);
    }

    public static String getTimeString(int hour, int minutes){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        Date date = cal.getTime();

        return sdf.format(date);
    }

    public static String getDateTitleFromDate(Date date){
        SimpleDateFormat titleFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return titleFormat.format(cal.getTime());
    }

}
