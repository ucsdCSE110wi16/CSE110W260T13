package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database.CalendarInfo;

public class UpdateEventsTask extends AsyncTask<Date, Void, ArrayList<CalendarEvent>>{
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private ContentResolver cr;

    public UpdateEventsTask(ContentResolver cr){
        this.cr = cr;
    }

    @Override
    protected ArrayList<CalendarEvent> doInBackground(Date... date) {
        //To Change Later
        ArrayList<CalendarEvent> returnList = new ArrayList<CalendarEvent>();

        for (int i = 0; i < date.length; i++) {
            //Find the upper bound for the query (the next day)
            Calendar cal = Calendar.getInstance();
            cal.setTime(date[i]);
            cal.add(Calendar.DATE, 1);

            //Convert it to string format for the db (unnecessary?)
            String startDate = formatter.format(date);
            String endDate = formatter.format(cal.getTime());

            //Query for all entries within date. Should be sorted by start time descending
            Cursor cursor = cr.query(CalendarContentProvider.CONTENT_URI,
                    CalendarInfo.FeedEntry.ALL_COLUMNS,
                    CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND " +
                            CalendarInfo.FeedEntry.DATE + " < '" + endDate + "'",
                    null,
                    CalendarInfo.FeedEntry.START_TIME + " DESC");

            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                //get the number of rows AKA number of events
                int numOfEvents = cursor.getCount();
                while(!cursor.isAfterLast()){
                    //Do stuff to set TextView of the event here
                    returnList.add(new CalendarEvent(CalendarInfo.FeedEntry.EVENT_TITLE,
                            CalendarInfo.FeedEntry.EVENT_DESCR,
                            CalendarInfo.FeedEntry.START_TIME,
                            CalendarInfo.FeedEntry.END_TIME));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        //Now we have a list of our CalendarEvent items
        return returnList;
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEventList){
        //We should update the UI here


    }
}
