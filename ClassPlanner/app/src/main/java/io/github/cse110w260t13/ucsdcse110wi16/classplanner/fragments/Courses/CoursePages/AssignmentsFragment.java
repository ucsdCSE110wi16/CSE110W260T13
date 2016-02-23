package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;

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
        return inflater.inflate(R.layout.fragment_course_assignment, container, false);
    }

    public void test(String course_name){
        Toast.makeText(getActivity().getBaseContext(),
                "assignments: + " + course_name, Toast.LENGTH_SHORT).show();
    }
}
