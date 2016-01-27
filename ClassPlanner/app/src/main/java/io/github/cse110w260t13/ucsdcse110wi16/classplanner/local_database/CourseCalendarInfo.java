package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;

import android.provider.BaseColumns;

public class CourseCalendarInfo {

    public CourseCalendarInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CourseList";
        public static final String COLUMN_COURSE_NAME = "CourseName";
        public static final String COLUMN_COURSE_LOC = "CourseLocation";
        public static final String COLUMN_START_TIME = "CourseStartTime";
        public static final String COLUMN_END_TIME = "CourseEndTime";

        public static final String COLUMN_MON = "ClassOnMon";
        public static final String COLUMN_TUE = "ClassOnTue";
        public static final String COLUMN_WED = "ClassOnWed";
        public static final String COLUMN_THUR = "ClassOnThur";
        public static final String COLUMN_FRI = "ClassOnFri";
        public static final String COLUMN_SAT = "ClassOnSat";
        public static final String COLUMN_SUN = "ClassOnSun";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME
            + " (" + FeedEntry._ID + " TEXT PRIMARY KEY," + FeedEntry.COLUMN_COURSE_NAME
            + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            FeedEntry.TABLE_NAME;
}
