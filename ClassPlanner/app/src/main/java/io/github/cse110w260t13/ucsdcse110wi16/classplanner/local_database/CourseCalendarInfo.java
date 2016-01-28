package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;

import android.provider.BaseColumns;

public class CourseCalendarInfo {

    public CourseCalendarInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CourseList";
        public static final String COLUMN_COURSE_NAME = "CourseName";
        public static final String COLUMN_COURSE_LOC = "CourseLocation";
        public static final String COLUMN_START_HOUR = "CourseStartHour";
        public static final String COLUMN_START_MIN = "CourseStartMin";
        public static final String COLUMN_END_HOUR = "CourseEndHour";
        public static final String COLUMN_END_MIN = "CourseEndMin";

        public static final String COLUMN_MON = "ClassOnMon";
        public static final String COLUMN_TUE = "ClassOnTue";
        public static final String COLUMN_WED = "ClassOnWed";
        public static final String COLUMN_THUR = "ClassOnThur";
        public static final String COLUMN_FRI = "ClassOnFri";
        public static final String COLUMN_SAT = "ClassOnSat";
        public static final String COLUMN_SUN = "ClassOnSun";
    }

}
