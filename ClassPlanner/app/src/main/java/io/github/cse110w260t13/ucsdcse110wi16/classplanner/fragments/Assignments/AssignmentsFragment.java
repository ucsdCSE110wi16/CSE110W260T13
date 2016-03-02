package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Assignments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentInfo;

public class AssignmentsFragment extends Fragment{


    private SQLiteDatabase db;
    private AssignmentDbHelper mDbHelper;

    private String CourseName;
    Spinner spin;

    private ListView listview;

    public AssignmentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_assignment, container, false);

        //Create and set the toolbar(Actionbar) for the Courses page
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //makes onClickListeners to add and delete things
        ImageButton add = (ImageButton) rootView.findViewById(R.id.add_button);
        ImageButton delete = (ImageButton) rootView.findViewById(R.id.delete_button);
        clickHandler click_handler = new clickHandler();
        add.setOnClickListener(click_handler);
        delete.setOnClickListener(click_handler);

        listview = (ListView)rootView.findViewById(R.id.assignment_list);
        updateData();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete an assignment");
                    builder.setMessage("Which assignment should be deleted?");
                    final EditText inputField = new EditText(getContext());
                    builder.setView(inputField);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = inputField.getText().toString();
                            ContentResolver cr = getActivity().getContentResolver();
                            cr.delete(AssignmentContentProvider.CONTENT_URI,
                                    AssignmentInfo.FeedEntry.ASSIGNMENT_NAME + "=?",
                                    new String[]{name});
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    break;
            }
            updateData();
        }
    }

    void updateData(){
        Log.i("updateData", " entered");
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(
                AssignmentContentProvider.CONTENT_URI,
                AssignmentInfo.FeedEntry.ALL_COLUMNS,
                null,
                null,
                null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.assignment_view,
                cursor,
                new String[]{AssignmentInfo.FeedEntry.COURSE_NAME,
                        AssignmentInfo.FeedEntry.ASSIGNMENT_NAME,
                        AssignmentInfo.FeedEntry.TYPE,
                        AssignmentInfo.FeedEntry.POINTS_EARNED,
                        AssignmentInfo.FeedEntry.POINTS_POSSIBLE},
                new int[]{R.id.CourseType,
                        R.id.AssignmentName,
                        R.id.AssignmentType,
                        R.id.PointsEarned,
                        R.id.PointsPossible}
                ,0);
        listview.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateData();
    }
}