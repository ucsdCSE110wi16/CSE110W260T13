package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hirondelle.date4j.DateTime;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.custom_views.CustomCaldroidFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.ChangeableColor;

/**
 * Fragment that will contain Calendar section's features.
 */
public class CalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = "CalendarFragment";
    private static final int COLOR_DELTA = 3;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


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

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.calendar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new calendar item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Create a Caldroid fragment
        CustomCaldroidFragment caldroidFragment = new CustomCaldroidFragment();
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

        // Set colors for dates
        Map<String, Object> caldroidData = caldroidFragment.getCaldroidData();

        /**
         * ToDo: find a way to populate data for the color change quickly
         */
        getDummyData(caldroidData);

        // Set colors for dates
        caldroidFragment.setBackgroundResourceForDateTimes(
                getDummyData(caldroidData)
        );

        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();

        //Create onClickListener for Caldroid
        final CaldroidListener listener = new CaldroidListener() {

            //OnSelectDate, I want events for that day to pop up.
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getActivity().getBaseContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                //db.query formatter.format(date)
                select(date);
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

    private HashMap<DateTime, Integer> getDummyData(Map<String, Object> caldroidData) {

        HashMap<DateTime, Integer> mappedColors = new HashMap<DateTime, Integer>();

        int[] eventsPerDayDummyData = {
                1, 3, 0, 7, 0, 4, 3,
                0, 5, 0, 1, 0, 11, 2,
                1, 0, 1, 0, 0, 20, 10,
                1, 4, 0, 3, 1, 2, 3,
                3, 0, 2, 9, 5, 2, 2,
                4, 1, 20, 4, 1, 1, 5,
        };

        DateTime today = CalendarHelper.convertDateToDateTime(new Date());

        ArrayList<DateTime> gridDayList = CalendarHelper.getFullWeeks(
                today.getMonth(),
                today.getYear(),
                (Integer) caldroidData
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

            for(int j = 0; j < eventsPerDayDummyData[i]; j++) {

                colorIntForDay = Long.decode(
                        changeableColor.darkenColorByDeltaPercent().toHex()
                ).intValue();

            }

            mappedColors.put(monthDayList.get(i), colorIntForDay);
        }

        return mappedColors;

    }


    public void select(Date date){
        ContentResolver cr = getActivity().getContentResolver();

        //Find the upper bound for the query (the next day)
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        //Convert it to string format for the db (unnecessary?)
        String startDate = formatter.format(date);
        String endDate = formatter.format(cal.getTime());

        //Query for all entries within date
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
            for(int i=0; i< numOfEvents; i++){
                //Do stuff to set textview of the event here
                cursor.moveToNext();
            }
        }
    }


    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = CalendarInfo.FeedEntry.ALL_COLUMNS;

        CursorLoader cursor_ld = new CursorLoader(getContext(),
                    CalendarContentProvider.CONTENT_URI, projection,
                    null, null, null);

        return cursor_ld;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    //Release any references/resources that are not needed anymore
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

}