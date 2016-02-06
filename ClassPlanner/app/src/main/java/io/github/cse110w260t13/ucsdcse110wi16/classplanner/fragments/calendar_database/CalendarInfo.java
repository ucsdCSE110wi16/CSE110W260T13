package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database;

import android.provider.BaseColumns;

public class CalendarInfo {

    public CalendarInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CalendarTable";
        public static final String DATE = "Date";
        public static final String START_TIME = "StartTime";
        public static final String END_TIME = "EndTime";
        public static final String EVENT_TITLE = "EventTitle";
        public static final String EVENT_DESCR = "EventDescr";
    }

}
