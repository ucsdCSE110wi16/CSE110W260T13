package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Fragment that will contain Calendar section's features.
 */
public class CalendarFragment extends Fragment {

    private int[] eventsPerDayDummyData = {
            1, 3, 1, 7, 1, 4, 3,
            1, 1, 1, 1, 1, 1, 2,
            1, 0, 1, 5, 0, 1, 0,
            1, 4, 1, 3, 1, 2, 0,
            3, 2, 2, 9, 5, 2, 2,
            4, 1, 1, 4, 1, 1, 0,
    };

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
        CaldroidFragment caldroidFragment = new CaldroidFragment();
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
        //caldroidFragment.setBackgroundColorForDate(0xFF00FF00, new Date());

        // Must refresh after changing the appearance of the View
        caldroidFragment.refreshView();

        return rootView;
    }
}