package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import hirondelle.date4j.DateTime;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil.CustomCaldroidFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEventsAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil.ChangeableColor;

/**
 * Fragment that will contain Calendar section's features.
 */
public class CalendarFragment extends Fragment{

    private static final String LOG_TAG = "CalendarFragment";
    private static final int COLOR_DELTA = 3;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final int URL_LOADER = 0;
    private ListView listView;
    private CalendarEventsAdapter adapter;

    private CustomCaldroidFragment caldroidFragment;
    private CheckBox personalTodoCheckbox;

    /**
     * Created upon entering Fragment's view creation stage.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.calendar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new calendar item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get the personal todolist checkbox
        personalTodoCheckbox = (CheckBox) rootView.findViewById(R.id.todo_checkBox);

        // Set the change listener to update the colors
        personalTodoCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            updateCalendarColors();
                    }
                }
        );

        // Create a Caldroid fragment
        caldroidFragment = new CustomCaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        // Replace the default calendar with the Caldroid calendar
        android.support.v4.app.FragmentTransaction t
                = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.caldroidContainer, caldroidFragment);
        t.commit();

        // Updates the calendar with the latest visualization
        this.updateCalendarColors();

        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();

        //assign listView's layout
        listView = (ListView) rootView.findViewById(R.id.List);

        //Create onClickListener for Caldroid
        final CaldroidListener listener = new CaldroidListener() {
            //OnSelectDate, I want events for that day to pop up.
            @Override
            public void onSelectDate(Date date, View view) {
                Log.d("onSelectDate: ", "shortpress");
                Toast.makeText(getActivity().getBaseContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                /**
                 * ToDo: Add ListView to layout. For now, go back to courses page
                 * and create functionality to add the course information into the
                 * calendar db before proceeding further.
                 */
                UpdateEventsTask eventUpdater = new UpdateEventsTask(
                        getActivity().getBaseContext(),
                        listView,
                        getActivity().getContentResolver());
                eventUpdater.execute(date, null, null);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getActivity().getBaseContext(), text,
                        Toast.LENGTH_SHORT).show();
                //When Months are changed, need to repopulate the colors
            }


            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getActivity().getBaseContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                //Use long click to edit events on a day?
            }

            @Override
            public void onCaldroidViewCreated() {
                Toast.makeText(getActivity().getBaseContext(),
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT).show();
                //Should automatically select Today
            }

        };
        caldroidFragment.setCaldroidListener(listener);

        return rootView;
    }

    /**
     * Resets and then updates the calendar visualization corresponding to the most recent data
     */
    private void updateCalendarColors() {

        // Reset colors for dates
        DateTime today = CalendarHelper.convertDateToDateTime(new Date());
        caldroidFragment.clearBackgroundResourceForDateTimes(
                CalendarHelper.getFullWeeks(
                        today.getMonth(),
                        today.getYear(),
                        (Integer) caldroidFragment.getCaldroidData()
                        .get(CaldroidFragment.START_DAY_OF_WEEK),
                        true
                )
        );

        Log.d(LOG_TAG, "calling updateCalendarColors");

        // Set colors for dates
        caldroidFragment.setBackgroundResourceForDateTimes(
                this.getCalendarColors()
        );

        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();
    }


    /**
     * Gets the colors for the calendar visualization and assigns them to DateTime objects
     *
     * @return DateTime objects assigned to color integers
     */
    private HashMap<DateTime, Integer> getCalendarColors() {

        HashMap<DateTime, Integer> mappedColors = new HashMap<DateTime, Integer>();

        // TODO use actual SQLite data
        int[] eventsPerDayDummyData = {
                1, 3, 0, 7, 0, 4, 3,
                0, 5, 0, 1, 0, 11, 2,
                1, 0, 1, 0, 0, 20, 10,
                1, 4, 0, 3, 1, 2, 3,
                3, 0, 2, 9, 5, 2, 2,
                4, 1, 20, 4, 1, 1, 5,
        };
        int[] eventsPerDayWithTodoListDummyData = {
                5, 3, 5, 7, 2, 4, 3,
                3, 5, 5, 3, 4, 12, 2,
                4, 3, 8, 3, 4, 25, 12,
                6, 4, 5, 3, 4, 2, 3,
                3, 4, 2, 9, 5, 2, 2,
                4, 3, 28, 4, 4, 5, 5,
        };
        int[] itemsPerDayInMonth;

        if(personalTodoCheckbox.isChecked()) {

            itemsPerDayInMonth = eventsPerDayWithTodoListDummyData;

        } else {

            itemsPerDayInMonth = eventsPerDayDummyData;

        }

        DateTime today = CalendarHelper.convertDateToDateTime(new Date());

        ArrayList<DateTime> gridDayList = CalendarHelper.getFullWeeks(
                today.getMonth(),
                today.getYear(),
                (Integer) caldroidFragment.getCaldroidData()
                        .get(CaldroidFragment.START_DAY_OF_WEEK),
                true
        );

        ArrayList<DateTime> monthDayList = new ArrayList<DateTime>();
        boolean firstDayOfMonthFound = false;

        // Remove days not in the current month
        for(int i = 0; i < gridDayList.size(); i++) {

            if(!firstDayOfMonthFound && (gridDayList.get(i).getDay() == 1)) {

                // Found the first day of the month, add it to the new list
                monthDayList.add(gridDayList.get(i));
                firstDayOfMonthFound = true;

            } else if(firstDayOfMonthFound && (gridDayList.get(i).getDay() != 1)) {

                // Found a day that isn't in next month, add it to the new list
                monthDayList.add(gridDayList.get(i));

            } else if(firstDayOfMonthFound && (gridDayList.get(i).getDay() == 1)) {

                // We've reached next month
                break;

            }

        }

        Log.d(LOG_TAG, gridDayList.get(0).getDay().toString());

        ChangeableColor changeableColor;
        int colorIntForDay;

        int color = ContextCompat.getColor(
                this.getContext(),
                R.color.colorPrimaryLight);

        int r =   (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b =  (color >> 0) & 0xFF;


        for(int i = 0; i < monthDayList.size(); i++) {

            changeableColor = new ChangeableColor(r, g, b, COLOR_DELTA);
            colorIntForDay = 0;

            for(int j = 0; j < itemsPerDayInMonth[i]; j++) {

                colorIntForDay = Long.decode(
                        changeableColor.darkenColorByDeltaPercent().toHex()
                ).intValue();

            }

            mappedColors.put(monthDayList.get(i), colorIntForDay);
        }

        return mappedColors;

    }


    private class UpdateEventsTask extends AsyncTask<Date, Void, ArrayList<CalendarEvent>> {
        private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());


        private ContentResolver cr;
        private Context context;
        private ListView listView;

        //Need context in order to create a new adapter. Pass it in from parent
        public UpdateEventsTask(Context context, ListView listView, ContentResolver cr) {
            super();
            this.cr = cr;
            this.context = context;
            this.listView = listView;
            Log.d("UpdateEventsTask: ", "constructor");

        }

        @Override
        protected ArrayList<CalendarEvent> doInBackground(Date... date) {
            Log.d("UpdateEventsTask: ", "doInBackground");
            //To Change Later
            ArrayList<CalendarEvent> returnList = new ArrayList<CalendarEvent>();
            Log.d("UpdateEventsTask: ", "doInBg  date param" + date[0]);

            //for (int i = 0; i < date.length; i++) {
                //Find the upper bound for the query (the next day)
            Calendar cal = Calendar.getInstance();
            cal.setTime(date[0]);
            Log.d("UpdateEventsTask: ", "doInBg  time" + date[0]);

            cal.add(Calendar.DATE, 1);

            //Convert it to string format for the db (unnecessary?)
            String startDate = dayFormat.format(date[0]);
            String endDate = dayFormat.format(cal.getTime());
            Log.d("UpdateEventsTask: ", "doInBg  start date" + startDate);
            Log.d("UpdateEventsTask: ", "doInBg  end date" + endDate);



            String daySelection = CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND "
                    + CalendarInfo.FeedEntry.DATE + " < '" + endDate + "'";

            //Query for all entries within date. Should be sorted by start time descending
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
                    int eventTitle = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TITLE);
                    int eventDesc = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_DESCR);
                    int startTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.START_TIME);
                    int endTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.END_TIME);
                    Log.d("UpdateEventsTask: ", "doInBg " + cursor.getString(eventTitle));

                    //Add every event to the ArrayList
                    returnList.add(new CalendarEvent(
                            cursor.getString(eventTitle),
                            cursor.getString(eventDesc),
                            cursor.getString(startTime),
                            cursor.getString(endTime)));
                    cursor.moveToNext();
                }
                cursor.close();
            }
            //}
            //Now we have a list of our CalendarEvent items
            for(CalendarEvent item: returnList){
                Log.d("UpdateEventsTask: ", "doInBg " + item.eventTitle);
            }
            return returnList;
        }

        @Override
        protected void onPostExecute(ArrayList<CalendarEvent> calendarEventList) {
            Log.d("UpdateEventsTask: ", " onPostExecute");

            //Create a new adapter if there is no prior instance
            if(adapter == null){
                adapter = new CalendarEventsAdapter(context, calendarEventList);
                listView.setAdapter(adapter);
            }
            //otherwise clear the adapter and re-add new events
            else {
                adapter.clear();
                for (CalendarEvent event : calendarEventList) {
                    adapter.add(event);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

}