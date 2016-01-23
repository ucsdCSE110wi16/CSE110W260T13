package io.github.cse110w260t13.ucsdcse110wi16.classplanner.navdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;


public class CoursesFragment extends Fragment{
    public CoursesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.courses_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Add a new course", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        return rootView;
    }

}
