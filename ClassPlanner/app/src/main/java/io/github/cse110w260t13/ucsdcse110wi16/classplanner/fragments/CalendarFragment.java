package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CalendarHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.custom_views.CustomCaldroidFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.ChangeableColor;

/**
 * Fragment that will contain Calendar section's features.
 */
public class CalendarFragment extends Fragment {

    private static final String LOG_TAG = "CalendarFragment";
    private static final int COLOR_DELTA = 3;

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

        // Updates the calendar with tje ;atest visualization
        this.updateCalendarColors();

        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();

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

}