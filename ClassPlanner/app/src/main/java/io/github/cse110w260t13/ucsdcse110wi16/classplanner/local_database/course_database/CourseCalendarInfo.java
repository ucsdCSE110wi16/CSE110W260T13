package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database;

import android.provider.BaseColumns;

public class CourseCalendarInfo {

    public CourseCalendarInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CourseList";
        public static final String COLUMN_COURSE_NAME = "CourseName";
        public static final String COLUMN_COURSE_LOC = "CourseLocation";
        public static final String COLUMN_START_TIME = "CourseStartTime";
        public static final String COLUMN_END_TIME = "CourseEndTime";
        public static final String COLUMN_END_DATE = "CourseEndDate";


        public static final String COLUMN_MON = "ClassOnMon";
        public static final String COLUMN_TUE = "ClassOnTue";
        public static final String COLUMN_WED = "ClassOnWed";
        public static final String COLUMN_THUR = "ClassOnThur";
        public static final String COLUMN_FRI = "ClassOnFri";
        public static final String COLUMN_SAT = "ClassOnSat";
        public static final String COLUMN_SUN = "ClassOnSun";

        public static final String COLUMN_NOTES = "ClassNotes";
        public static final String COLUMN_INSTR_NAME = "ClassInstr";
        public static final String COLUMN_INSTR_EMAIL = "InstrEmail";
        public static final String COLUMN_WEBSITE = "ClassSite";

        public static final String[] ALL_COLUMNS= {
                _ID,
                COLUMN_COURSE_NAME,
                COLUMN_COURSE_LOC,
                COLUMN_START_TIME,
                COLUMN_END_TIME,
                COLUMN_END_DATE,
                COLUMN_SUN,
                COLUMN_MON,
                COLUMN_TUE,
                COLUMN_WED,
                COLUMN_THUR,
                COLUMN_FRI,
                COLUMN_SAT,
                COLUMN_NOTES,
                COLUMN_INSTR_NAME,
                COLUMN_INSTR_EMAIL,
                COLUMN_WEBSITE,
        };
    }

}
