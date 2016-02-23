package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Calendar.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            CalendarInfo.FeedEntry.TABLE_NAME + " (" +
            CalendarInfo.FeedEntry._ID + " INTEGER PRIMARY KEY," +
            CalendarInfo.FeedEntry.DATE + " TEXT," +
            CalendarInfo.FeedEntry.START_TIME + " TEXT," +
            CalendarInfo.FeedEntry.END_TIME + " TEXT," +
            CalendarInfo.FeedEntry.EVENT_TITLE + " TEXT," +
            CalendarInfo.FeedEntry.EVENT_DESCR + " TEXT," +
            CalendarInfo.FeedEntry.EVENT_TYPE + " TEXT" +  " ) ";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            CalendarInfo.FeedEntry.TABLE_NAME;

    public CalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}