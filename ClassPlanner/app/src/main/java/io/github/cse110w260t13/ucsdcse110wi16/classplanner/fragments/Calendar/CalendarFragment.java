package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.DateTimeUtil;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil.CaldroidCustomFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.AddCalendarDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarRecyclerAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.EditDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

/**
 * Fragment that implements Calendar section's features.
 */
public class CalendarFragment extends Fragment implements CalendarRecyclerAdapter.RecyclerAdapterCallback{

    //creating static strings and ints
    private static final String LOG_TAG = "CalendarFragment";
    private static final int REQUEST_CODE = 0;

    public static final String EVENT_TYPE_EVENT = "event";
    public static final String EVENT_TYPE_CLASS = "class";
    public static final String EVENT_TYPE_HOMEWORK = "homework";
    public static final String EVENT_TYPE_TODO = "todo";
    
    //making format, list view, adapter, other features
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private RecyclerView list;
    private CalendarRecyclerAdapter adapter;

    private CaldroidFragment caldroidFragment;
    private CheckBox homeworkCheckbox;
    private CheckBox classCheckbox;
    private Date daySelected;

    private Drawable[] calendarColors;
    private int[] calendarColorsRes;

    private CoordinatorLayout coordinatorLayout;

    private int caldroidSelectedMonth;
    private int caldroidSelectedYear;

    private View rootView;
    
    /**
     * onCreateEditDialog method to create dialog when edit is chosen. 
     * @param id to edit
     */
    @Override
    public void onCreateEditDialog(String id){
        //creates dialog
        Log.i("CalendarCallback", "onCreateEditDialog start");
        DialogFragment dialog = new EditDialogFragment();
        //sets and shows dialog
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("day", daySelected.toString());
        dialog.setArguments(args);
        dialog.setTargetFragment(this, REQUEST_CODE);
        dialog.show(getFragmentManager(), "EditDialogFragment");
    }
    
    /**
     * onDeleteOK method to update calendar colors when item is deleted
     */
    @Override
    public void onDeleteOK() {
        updateCalendarColors();
    }
    
    /**
     * onActivityResult method does stuff depending on the request and result
     * @param requestCode that was requested
     * @param resultCode that results
     * @param data intent operation to be performed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Stuff to do, dependent on requestCode and resultCode
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.i("CalendarResult", "Submit pressed on dialog");
            UpdateEventsTask eventUpdater = new UpdateEventsTask(
                    getActivity().getBaseContext(),
                    list,
                    getActivity().getContentResolver());
            eventUpdater.execute(daySelected, null, null);
        }
    }

    /**-------------------------------------------------------------------------------------------
     * onCreateView Created upon entering Fragment's view creation stage.
     * @param inflater to instantiate layout into view
     * @param container to contain views
     * @param savedInstanceState saved state of fragment
     *-------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.fragment_calendar_coordinator_layout);

        /*******************************SETTING UP THE TOOLBAR************************************/
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*******************************SETTING UP THE FAB*****************************************/
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.calendar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display new calendar item dialog
                DialogFragment dialog = new AddCalendarDialogFragment();
                Bundle args = new Bundle();
                args.putString("day", daySelected.toString());
                dialog.setArguments(args);
                //Set target fragment to CalendarFragment for dialog.
                dialog.setTargetFragment(CalendarFragment.this, REQUEST_CODE);
                dialog.show(getFragmentManager(), "AddCalendarDialogFragment");
            }
        });

        /*******************************SETTING UP CHECKBOXES*************************************/

        // Get the personal todolist checkbox
        homeworkCheckbox = (CheckBox) rootView.findViewById(R.id.homework_checkbox);

        // Set the change listener to update the colors
        homeworkCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        updateCalendarColors();
                    }
                }
        );

        // Get the personal todolist checkbox
        classCheckbox = (CheckBox) rootView.findViewById(R.id.class_checkbox);

        // Set the change listener to update the colors
        classCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        updateCalendarColors();
                    }
                }
        );

        /****************************SETTING UP CALDROID FRAGMENT**********************************/
        // Create colors list as Drawables
        calendarColors = new Drawable[] {
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_1),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_2),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_3),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_4),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_5),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_6),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_7),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_8),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_9),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_10),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_11),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_12),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_13),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_14),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_15),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_16),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_17),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_18),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_19),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_20),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_21),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_22),
                ContextCompat.getDrawable(this.getContext(), R.color.calendar_23),
        };

        // Create colors list as Resources
        calendarColorsRes = new int[] {
                R.color.calendar_1,
                R.color.calendar_2,
                R.color.calendar_3,
                R.color.calendar_4,
                R.color.calendar_5,
                R.color.calendar_6,
                R.color.calendar_7,
                R.color.calendar_8,
                R.color.calendar_9,
                R.color.calendar_10,
                R.color.calendar_11,
                R.color.calendar_12,
                R.color.calendar_13,
                R.color.calendar_14,
                R.color.calendar_15,
                R.color.calendar_16,
                R.color.calendar_17,
                R.color.calendar_18,
                R.color.calendar_19,
                R.color.calendar_20,
                R.color.calendar_21,
                R.color.calendar_22,
                R.color.calendar_23,
        };

        // Create a Caldroid fragment
        caldroidFragment = new CaldroidCustomFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);

        // Replace the default calendar with the Caldroid calendar
        android.support.v4.app.FragmentTransaction t
                = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.caldroidContainer, caldroidFragment);
        t.commit();

        //Create onClickListener for Caldroid
        final CaldroidListener listener = new CustomCaldroidListener();
        caldroidFragment.setCaldroidListener(listener);

        /****************************SETTING UP EVENTS DISPLAY*************************************/
        //assign listView's layout
        list = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CalendarRecyclerAdapter(this, null, getActivity());
        list.setAdapter(adapter);

        Date today = new Date();
        daySelected = today;
        caldroidSelectedYear = DateTimeUtil.getDateFromDate(today, DateTimeUtil.YEAR);
        caldroidSelectedMonth = DateTimeUtil.getDateFromDate(today, DateTimeUtil.MONTH);
        classCheckbox.setChecked(true);

        TextView dateTitle = (TextView) rootView.findViewById(R.id.calendar_date_title);
        dateTitle.setText(DateTimeUtil.getDateTitleFromDate(daySelected));

        UpdateEventsTask eventUpdater = new UpdateEventsTask(
                getActivity().getBaseContext(),
                list,
                getActivity().getContentResolver());
        eventUpdater.execute(today, null, null);

        return rootView;
    }

    /**-------------------------------------------------------------------------------------------
     * CustomCaldroidListener that responds to a number of user interactions with the calendar
     *-------------------------------------------------------------------------------------------*/
    private class CustomCaldroidListener extends CaldroidListener{
        /**
         * onSelectDate method that displays view of events happening on a date
         * @param date chosen
         * @param view to be displayed
         */
        @Override
        public void onSelectDate(Date date, View view) {
            Log.d("onSelectDate: ", "shortpress");
            //making snackbar to display information for that date.
            Snackbar.make(coordinatorLayout, "Showing events for "
                    + formatter.format(date), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
                    
            //making updater for date
            UpdateEventsTask eventUpdater = new UpdateEventsTask(
                    getActivity().getBaseContext(),
                    list,
                    getActivity().getContentResolver());
            eventUpdater.execute(date, null, null);

            //updateCalendarColors();
            
            //creating the view for the events happening that day
            daySelected = date;
            TextView dateTitle = (TextView) rootView.findViewById(R.id.calendar_date_title);
            dateTitle.setText(DateTimeUtil.getDateTitleFromDate(daySelected));
        }
        
        /**
         * onChangeMonth method that repopulates colors for calendar when months are changed
         * @param month selected
         * @param year selected
         */
        @Override
        public void onChangeMonth(int month, int year) {

            caldroidSelectedMonth = month;
            caldroidSelectedYear = year;
            updateCalendarColors();

            //When Months are changed, need to repopulate the colors
        }
        
        /**
         * onLongClickDate method to show view for when dates are long clicked.
         * Currently not implemented.
         * @param date being long clicked
         * @param view being shown
         */
        @Override
        public void onLongClickDate(Date date, View view) {
            //Use long click to edit events on a day?
            //updateCalendarColors();
        }
        
        /**
         * onCaldroidViewCreated for when a caldroid calendar view is created
         */
        @Override
        public void onCaldroidViewCreated() {
            // Set custom arrow colors
            caldroidFragment.getLeftArrowButton().setBackgroundResource(R.drawable.left_arrow);
            caldroidFragment.getRightArrowButton().setBackgroundResource(R.drawable.right_arrow);
            //hideHeader(caldroidFragment);

            // Updates the calendar with the latest visualization
            updateCalendarColors();
        }
    }

    /**-------------------------------------------------------------------------------------------
     * Method to hide the header of the Caldroid Calendar (the months).
     * CURRENTLY UNUSED
     *-------------------------------------------------------------------------------------------*/
    private void hideHeader(CaldroidFragment fragCalendar) {
        if(fragCalendar != null) {
            fragCalendar.getMonthTitleTextView().setVisibility(View.GONE);
            fragCalendar.getLeftArrowButton().setVisibility(View.GONE);
            fragCalendar.getRightArrowButton().setVisibility(View.GONE);
        }
    }

    /**-------------------------------------------------------------------------------------------
     * Resets and then updates the calendar visualization corresponding to the most recent data
     *-------------------------------------------------------------------------------------------*/
    @SuppressWarnings("deprecation")
    private void updateCalendarColors() {

        ArrayList<DateTime> selectedMonth = CalendarHelper.getFullWeeks(
                caldroidSelectedMonth,
                caldroidSelectedYear,
                (Integer) caldroidFragment.getCaldroidData()
                        .get(CaldroidFragment.START_DAY_OF_WEEK),
                true
        );

        // Reset colors for dates
        caldroidFragment.clearBackgroundDrawableForDateTimes(
                selectedMonth
        );

        // Reset text color for Date
        for(int i = 0; i <= selectedMonth.size() - 1; i++) {

            caldroidFragment.setTextColorForDateTime(R.color.caldroid_gray, selectedMonth.get(i));

        }

        Log.d(LOG_TAG, "calling updateCalendarColors");

        // Set colors for dates
        caldroidFragment.setBackgroundDrawableForDateTimes(
                this.getCalendarColors()
        );

        // Should automatically select Today
        // By default, Date() gets the time at which it was allocated
        Date dateToday = new Date();

        caldroidFragment.setSelectedDate(dateToday);
        caldroidFragment.setBackgroundDrawableForDate(
                ContextCompat.getDrawable(this.getContext(), R.drawable.red_border), dateToday);
        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();
    }

    /**-------------------------------------------------------------------------------------------
     * Gets the colors for the calendar visualization and assigns them to DateTime objects
     *
     * @return DateTime objects assigned to color integers
     *-------------------------------------------------------------------------------------------*/
    private HashMap<DateTime, Drawable> getCalendarColors() {

        HashMap<DateTime, Drawable> mappedColors = new HashMap<DateTime, Drawable>();

        DateTime today = CalendarHelper.convertDateToDateTime(new Date());

        ArrayList<DateTime> gridDayList = CalendarHelper.getFullWeeks(
                caldroidSelectedMonth,
                caldroidSelectedYear,
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
            }
            else if(firstDayOfMonthFound && (gridDayList.get(i).getDay() != 1)) {
                // Found a day that isn't in next month, add it to the new list
                monthDayList.add(gridDayList.get(i));
            }
            else if(firstDayOfMonthFound && (gridDayList.get(i).getDay() == 1)) {
                // We've reached next month
                break;
            }
        }
        
        //handles cases for when user chooses homework or class items checkbox
        int[] itemsPerDayInMonth = getItemsPerDayFromMonth(monthDayList, EVENT_TYPE_EVENT);
        int[] homeworkItemsPerDayInMonth = new int[monthDayList.size()];
        int[] classItemsPerDayInMonth = new int[monthDayList.size()];

        if(homeworkCheckbox.isChecked()) {
            homeworkItemsPerDayInMonth = getItemsPerDayFromMonth(monthDayList, EVENT_TYPE_HOMEWORK);
        }

        if(classCheckbox.isChecked()) {
            classItemsPerDayInMonth = getItemsPerDayFromMonth(monthDayList, EVENT_TYPE_CLASS);
        }

        for(int i = 0; i <= itemsPerDayInMonth.length - 1; i++) {
            itemsPerDayInMonth[i] +=
                    + homeworkItemsPerDayInMonth[i]
                    + classItemsPerDayInMonth[i];
        }
        
        //mapping the colors for the days in the month
        int colorIndex;

        for(int i = 0; i < monthDayList.size(); i++) {

            if(itemsPerDayInMonth[i] >= calendarColors.length) {
                colorIndex = calendarColors.length - 1;
            } else {
                colorIndex = itemsPerDayInMonth[i];
            }

            mappedColors.put(monthDayList.get(i), calendarColors[colorIndex]);

            if(monthDayList.get(i).equals(today)) {
                caldroidFragment.setTextColorForDate(
                        calendarColorsRes[colorIndex],
                        new Date()
                );
            } else {
                caldroidFragment.setTextColorForDateTime(
                        android.R.color.white,
                        monthDayList.get(i)
                );
            }

        }
        return mappedColors;
    }

    /**
     * Generates the number of events per day in the given month.
     *
     * @param month the days of the month
     * @param eventType the type of event to be queried for
     * @return corresponding number of events per month day
     */
    @SuppressWarnings("deprecation")
    private int[] getItemsPerDayFromMonth(ArrayList<DateTime> month, String eventType) {

        int[] itemsPerDayFromMonth = new int[month.size()];

        String startDate = month.get(0).format("YYYY-MM-DD");
        String endDate = month.get(month.size() - 1).format("YYYY-MM-DD");

        String daySelection = CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND "
                + CalendarInfo.FeedEntry.DATE + " <= '" + endDate + "' AND "
                + CalendarInfo.FeedEntry.EVENT_TYPE + " == '" + eventType + "'";

        Log.d(LOG_TAG, daySelection);

        //Query for all entries within date. Should be sorted by start time descending
        Cursor cursor = getActivity().getContentResolver().query(CalendarContentProvider.CONTENT_URI,
                CalendarInfo.FeedEntry.ALL_COLUMNS,
                daySelection,
                null,
                CalendarInfo.FeedEntry.DATE + " ASC");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            for(int i = 0; (i <= month.size() - 1) && (!cursor.isAfterLast()); i++) {

                while((!cursor.isAfterLast()) &&
                        month.get(i).format("YYYY-MM-DD")
                                .equals(cursor.getString(
                                        cursor.getColumnIndex(
                                                CalendarInfo.FeedEntry.DATE)))) {

                    itemsPerDayFromMonth[i] += 3;
                    cursor.moveToNext();
                }

            }

            cursor.close();
        }


        return itemsPerDayFromMonth;
    }

    /**-------------------------------------------------------------------------------------------
     * Private AsyncTask that specifically updates and loads the events of each DAY onto the
     * recyclerView initialized for the CalendarFragment
     *-------------------------------------------------------------------------------------------*/
    private class UpdateEventsTask extends AsyncTask<Date, Void, ArrayList<CalendarEvent>> {
        private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        private ContentResolver cr;
        private Context context;
        private RecyclerView list;

        //Need context in order to create a new adapter. Pass it in from parent
        public UpdateEventsTask(Context context, RecyclerView list, ContentResolver cr) {
            super();
            this.cr = cr;
            this.context = context;
            this.list = list;
        }

        @Override
        protected ArrayList<CalendarEvent> doInBackground(Date... date) {
            ArrayList<CalendarEvent> returnList = new ArrayList<CalendarEvent>();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date[0]);
            Log.d("UpdateEventsTask: ", "doInBg  time" + date[0]);

            cal.add(Calendar.DATE, 1);

            String startDate = dayFormat.format(date[0]);
            String endDate = dayFormat.format(cal.getTime());
            Log.d("UpdateEventsTask: ", "doInBg  end date" + endDate);

            String daySelection = CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND "
                    + CalendarInfo.FeedEntry.DATE + " < '" + endDate + "'";

            //Query for all entries within date. Should be sorted by start time descending
            Cursor cursor = cr.query(CalendarContentProvider.CONTENT_URI,
                    CalendarInfo.FeedEntry.ALL_COLUMNS,
                    daySelection,
                    null,
                    CalendarInfo.FeedEntry.START_TIME + " ASC");

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int eventTitle = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TITLE);
                    int eventDesc = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_DESCR);
                    int startTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.START_TIME);
                    int endTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.END_TIME);
                    int eventID = cursor.getColumnIndex(CalendarInfo.FeedEntry._ID);
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
            return returnList;
        }
        
        /**
         * onPostExecute method creates an adapter if one does not exist and adds new events.
         * @param calendarEventList an arraylist containing calendar events.
         */
        @Override
        protected void onPostExecute(ArrayList<CalendarEvent> calendarEventList) {
            Log.d("UpdateEventsTask: ", " onPostExecute");
            updateCalendarColors();

            //Create a new adapter if there is no prior instance
            if(adapter == null){
                Log.d("UpdateEventsTask: ", " test context");
                adapter = new CalendarRecyclerAdapter(CalendarFragment.this, calendarEventList, context);
                list.setAdapter(adapter);
            }
            //otherwise clear the adapter and re-add new events
            else {
                adapter.swap(calendarEventList);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
