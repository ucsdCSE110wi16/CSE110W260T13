package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class AssignmentsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_assignments, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void test(String course_name){
        Toast.makeText(getActivity().getBaseContext(),
                "assignments: + " + course_name, Toast.LENGTH_SHORT).show();
    }
}