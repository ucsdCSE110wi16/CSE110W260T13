package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Assignments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentInfo;

public class AssignmentsFragment extends Fragment{


    private SQLiteDatabase db;
    private AssignmentDbHelper mDbHelper;

    private String AssignmentName;

    private ListView listview;

    public AssignmentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_assignments, container, false);
        //makes onClickListeners to add and delete things
        ImageButton add = (ImageButton) rootView.findViewById(R.id.add_button);
        ImageButton delete = (ImageButton) rootView.findViewById(R.id.delete_button);
        clickHandler click_handler = new clickHandler();
        add.setOnClickListener(click_handler);
        delete.setOnClickListener(click_handler);

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(AssignmentContentProvider.CONTENT_URI,
                AssignmentInfo.FeedEntry.ALL_COLUMNS,
                AssignmentInfo.FeedEntry.ASSIGNMENT_NAME + "=?",
                null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(),0,cursor,
                new String[]{AssignmentInfo.FeedEntry.ASSIGNMENT_NAME},
                new int[]{android.R.id.text1},0);
        listview.setAdapter(adapter);


        return rootView;
    }


    /**
     * click handler for all buttons in the Assignments Page
     */
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_button:
                    Intent intent = new Intent(getContext(), AddAssignment.class);
                    intent.putExtra("mode", "create");
                    startActivity(intent);
                    break;
                case R.id.delete_button:
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