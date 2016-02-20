package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AssignmentDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Assignment.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            AssignmentInfo.FeedEntry.NAME + " (" +
            AssignmentInfo.FeedEntry._ID + " INTEGER PRIMARY KEY," +
            AssignmentInfo.FeedEntry.COURSE_NAME + " TEXT," +
            AssignmentInfo.FeedEntry.ASSIGNMENT_NAME + " TEXT," +
            AssignmentInfo.FeedEntry.TYPE + " TEXT," +
            AssignmentInfo.FeedEntry.POINTS_POSSIBLE + " INTEGER," +
            AssignmentInfo.FeedEntry.POINTS_EARNED + " INTEGER," +
            AssignmentInfo.FeedEntry.NOTES + "TEXT" +
            " ) ";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            AssignmentInfo.FeedEntry.NAME;

    public AssignmentDbHelper(Context context) {
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

