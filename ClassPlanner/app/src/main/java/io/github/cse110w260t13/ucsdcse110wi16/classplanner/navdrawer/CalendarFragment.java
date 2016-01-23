package io.github.cse110w260t13.ucsdcse110wi16.classplanner.navdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Fragment that will contain Calendar section's features.
 */
public class CalendarFragment extends Fragment {
    private CalendarView calendar;

    public CalendarFragment() {
    }

    //Created upon entering Fragment's view creation stage.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendar = (CalendarView) rootView.findViewById(R.id.calendarView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.calendar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new calendar item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }
}