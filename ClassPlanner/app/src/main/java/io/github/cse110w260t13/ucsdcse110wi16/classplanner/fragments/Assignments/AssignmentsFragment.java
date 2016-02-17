package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Assignments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentInfo;

public class AssignmentsFragment extends Fragment{


    private SQLiteDatabase db;
    private AssignmentDbHelper mDbHelper;

    private String AssignmentName;

    public AssignmentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        ImageButton add = (ImageButton) rootView.findViewById(R.id.add_course_button);
        ImageButton delete = (ImageButton) rootView.findViewById(R.id.delete_course_button);
        clickHandler click_handler = new clickHandler();
        add.setOnClickListener(click_handler);
        delete.setOnClickListener(click_handler);

        return rootView;
    }


    /**
     * click handler for all buttons in the Assignments Page
     */
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_course_button:
                    startActivity(new Intent(getActivity(), AddAssignment.class));
                    break;
                case R.id.delete_course_button:
                    Log.d("on click", AssignmentName);
                    ContentResolver cr = getActivity().getContentResolver();
                    cr.delete(AssignmentContentProvider.CONTENT_URI,
                            AssignmentInfo.FeedEntry.ASSIGNMENT_NAME + "=?",
                            new String[]{AssignmentName});
                    break;
            }
        }
    }


}