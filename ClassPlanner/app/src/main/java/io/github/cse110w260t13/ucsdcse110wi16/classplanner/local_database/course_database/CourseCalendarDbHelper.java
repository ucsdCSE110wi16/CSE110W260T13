package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseCalendarDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "courseCalendar.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            CourseCalendarInfo.GeneralInfo.TABLE_NAME + " (" +
            CourseCalendarInfo.GeneralInfo._ID + " INTEGER PRIMARY KEY," +
            CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_LOC + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_START_TIME + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_END_TIME + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_END_DATE + " TEXT," +

            CourseCalendarInfo.GeneralInfo.COLUMN_SUN + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_MON + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_TUE + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_WED + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_THUR + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_FRI + " INTEGER," +
            CourseCalendarInfo.GeneralInfo.COLUMN_SAT + " INTEGER," +

            CourseCalendarInfo.GeneralInfo.COLUMN_NOTES + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_NAME+ " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_EMAIL + " TEXT," +
            CourseCalendarInfo.GeneralInfo.COLUMN_WEBSITE + " TEXT" +
            " ) ";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            CourseCalendarInfo.GeneralInfo.TABLE_NAME;

    public CourseCalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

