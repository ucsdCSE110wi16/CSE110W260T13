package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

public class TestCalendarDbHelper {
    private static String ACCOUNT_NAME = "LocalUser";
    private static String CALENDAR_NAME = "ClassPlannerCalender";
    private static long CAL_ID;

    /**The main/basic URI for the android calendars table*/
    private static final Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;

    /**The main/basic URI for the android events table*/
    private static final Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;

    /**Creates the values the new calendar will have*/
    private static ContentValues buildNewCalContentValues() {
        final ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);

        //user can only read the calendar
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }

    /**Builds the Uri for your Calendar in android database (as a Sync Adapter)*/
    private static Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    /**Create and insert new calendar into android database
     * @param ctx The context (e.g. activity)
     */
    public static void createCalendar(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();
        Uri calUri = buildCalUri();
        //insert the calendar into the database
        cr.insert(calUri, cv);

        Uri newUri = cr.insert(buildCalUri(), cv);
        CAL_ID = Long.parseLong(newUri.getLastPathSegment());
    }

    /**Permanently deletes our calendar from database (along with all events)*/
    public static void deleteCalendar(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        Uri calUri = ContentUris.withAppendedId(buildCalUri(), CAL_ID);
        cr.delete(calUri, null, null);
    }

    /**Create event - Add an event to our calendar
     * @param dtstart Event start time (in millis)
     * @param dtend Event end time (in millis)
     */
    public static void addEvent(Context ctx, String title, String description, String location,
                                long dtstart, long dtend) {
        ContentResolver cr = ctx.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID, CAL_ID);
        cv.put(CalendarContract.Events.TITLE, title);
        cv.put(CalendarContract.Events.DTSTART, dtstart);
        cv.put(CalendarContract.Events.DTEND, dtend);
        cv.put(CalendarContract.Events.EVENT_LOCATION, location);
        cv.put(CalendarContract.Events.DESCRIPTION, description);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        cr.insert(buildEventUri(), cv);
    }

    /**Builds the Uri for events (as a Sync Adapter)*/
    public static Uri buildEventUri() {
        return EVENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }
}
