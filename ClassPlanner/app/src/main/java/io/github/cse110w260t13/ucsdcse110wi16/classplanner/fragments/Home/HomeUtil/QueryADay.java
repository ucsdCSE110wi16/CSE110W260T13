package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home.HomeUtil;

import android.database.Cursor;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

/**
 * Created by Bryan Yang on 2/21/2016.
 */
public class QueryADay {
    protected ArrayList<CalendarEvent> doInBackground(Date... date) {
        ArrayList<CalendarEvent> returnList = new ArrayList<CalendarEvent>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date[0]);

        cal.add(Calendar.DATE, 1);

        //Convert it to string format for the db (unnecessary?)
        Formatter dayFormat = null;
        Formatter startDate = dayFormat.format(String.valueOf(date[0]));
        Formatter endDate = dayFormat.format(String.valueOf(cal.getTime()));

        String daySelection = CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND "
                + CalendarInfo.FeedEntry.DATE + " < '" + endDate + "'";

        //Query for all entries within date. Should be sorted by start time descending
        AssignmentContentProvider cr = null;
        Cursor cursor = cr.query(CalendarContentProvider.CONTENT_URI,
                CalendarInfo.FeedEntry.ALL_COLUMNS,
                daySelection,
                null,
                CalendarInfo.FeedEntry.START_TIME + " DESC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            //get the number of rows AKA number of events
            int numOfEvents = cursor.getCount();

            while (!cursor.isAfterLast()) {
                int eventID = cursor.getColumnIndex(CalendarInfo.FeedEntry._ID);
                int eventTitle = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TITLE);
                int eventDesc = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_DESCR);
                int startTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.START_TIME);
                int endTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.END_TIME);
                int eventType = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TYPE);
                Log.d("UpdateEventsTask: ", "doInBg " + cursor.getString(eventTitle));

                //Add every event to the ArrayList

                returnList.add(new CalendarEvent(
                        cursor.getString(eventID),
                        cursor.getString(eventType),
                        cursor.getString(eventTitle),
                        cursor.getString(eventDesc),
                        cursor.getString(startTime),
                        cursor.getString(endTime)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        //Now we have a list of our CalendarEvent items
        for(CalendarEvent item: returnList){
            Log.d("UpdateEventsTask: ", "doInBg " + item.eventTitle);
        }
        return returnList;
    }
}
