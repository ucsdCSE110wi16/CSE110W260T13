package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database;

import android.provider.BaseColumns;

public class AssignmentInfo {

    public AssignmentInfo(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String NAME = "AssignmentList";
        public static final String COURSE_NAME = "CourseName";
        public static final String ASSIGNMENT_NAME = "AssignmentName";
        public static final String TYPE = "Type";

        public static final String POINTS_EARNED = "PointsEarned";
        public static final String POINTS_POSSIBLE = "PointsPossible";

        public static final String DUE_DATE = "DueDate";

        public static final String[] ALL_COLUMNS= {
                _ID,
                COURSE_NAME,
                ASSIGNMENT_NAME,
                TYPE,
                POINTS_EARNED,
                POINTS_POSSIBLE,
                DUE_DATE,
        };
    }

}
