package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;

public class CalendarContentProvider extends ContentProvider {
    private CourseCalendarDbHelper db_helper;
    private SQLiteDatabase db;

    //used for UriMatcher

    private static final int EVENT_INFO = 1;
    private static final int EVENT_ID = 2;


    private static final String AUTHORITY = "io.github.cse110w260t13.ucsdcse110wi16.classplanner.calendar_database";
    private static final String BASE_PATH = "Events";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/multiple";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/single";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, EVENT_INFO);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH+ "/#", EVENT_ID);
    }

    @Override
    public boolean onCreate() {
        db_helper = new CourseCalendarDbHelper(getContext());
        db = db_helper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //check to see if the columns requests by the user exist
        checkColumns(projection);

        queryBuilder.setTables(CalendarInfo.FeedEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch(uriType) {
            case EVENT_INFO:
                break;
            case EVENT_ID:
                queryBuilder.appendWhere(CalendarInfo.FeedEntry._ID + "=" +
                        uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        //makes sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri){
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;

        switch (uriType) {
            case EVENT_INFO:
                id = db.insert(CalendarInfo.FeedEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType) {
            case EVENT_INFO:
                rowsDeleted = db.delete(CalendarInfo.FeedEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(CalendarInfo.FeedEntry.TABLE_NAME,
                            CalendarInfo.FeedEntry._ID + "=" + id, null);
                }
                else {
                    rowsDeleted = db.delete(CalendarInfo.FeedEntry.TABLE_NAME,
                            CalendarInfo.FeedEntry._ID + "=" + id + "and"
                                    + selection, selectionArgs);
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;

        switch (uriType) {
            case EVENT_INFO:
                rowsUpdated = db.update(CalendarInfo.FeedEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(CalendarInfo.FeedEntry.TABLE_NAME, values,
                            CalendarInfo.FeedEntry._ID + "=" + id, null);
                }
                else {
                    rowsUpdated = db.update(CourseCalendarInfo.FeedEntry.TABLE_NAME, values,
                            CalendarInfo.FeedEntry._ID + "=" + id + "and"
                                    + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = CalendarInfo.FeedEntry.ALL_COLUMNS;
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
