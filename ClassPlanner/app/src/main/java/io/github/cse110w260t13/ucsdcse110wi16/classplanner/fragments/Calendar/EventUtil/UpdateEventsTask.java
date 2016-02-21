/**
 * MOVED TO PRIVATE CLASS IN CALENDAR FRAGMENT.
 * Issues with variable access and whatnot.
 * May move back to separate class in the future.
 * Will leave this in files for now.
 */

package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

public class UpdateEventsTask extends AsyncTask<Date, Void, ArrayList<CalendarEvent>>{
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private ContentResolver cr;
    private Context context;
    private ListView listView;

    //Need context in order to create a new adapter. Pass it in from parent
    public UpdateEventsTask(Context context, ListView listView, ContentResolver cr){
        super();
        this.cr = cr;
        this.context = context;
        this.listView = listView;
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
                    returnList.add(new CalendarEvent(
                            CalendarInfo.FeedEntry._ID,
                            CalendarInfo.FeedEntry.EVENT_TITLE,
                            CalendarInfo.FeedEntry.EVENT_DESCR,
                            CalendarInfo.FeedEntry.START_TIME
                    ));
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
        CalendarEventsAdapter adapter = new CalendarEventsAdapter(context, calendarEventList);
        listView.setAdapter(adapter);
    }
}
