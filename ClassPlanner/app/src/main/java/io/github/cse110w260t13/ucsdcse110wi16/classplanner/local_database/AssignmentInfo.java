package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;

import android.provider.BaseColumns;

public class AssignmentInfo {

    public AssignmentInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "AssignmentList";
        public static final String COLUMN_COURSE_NAME = "CourseName";
        public static final String COLUMN_ASSIGNMENT_NAME = "AssignmentName";

        public static final String ASSIGNMENT_TYPE = "Type";
        public static final String POINTS_EARNED = "PointsEarned";
        public static final String POINTS_TOTAL = "PointsTotal";
        public static final String PERCENTAGE = "Percentage";
        public static final String NOTES = "Notes";

        public static final String[] ALL_COLUMNS= {
                _ID,
                COLUMN_COURSE_NAME,
                COLUMN_ASSIGNMENT_NAME,
                ASSIGNMENT_TYPE,
                POINTS_EARNED,
                POINTS_TOTAL,
                PERCENTAGE,
                NOTES
        };
    }

}
