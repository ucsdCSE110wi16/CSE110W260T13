package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.AddClassActivity;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.AssignmentsFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.ClassInfoFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.TabsAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;

public class AssignmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TabHost assignmentTabHost;
    private ViewPager assignmentViewPager;

    private SimpleCursorAdapter adapter;
    private String assignment;

    private SQLiteDatabase db;
    private CourseCalendarDbHelper mDbHelper;

    public AssignmentFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_assignments, container, false);

        //TabHost is the container for a tabbed window view
        assignmentTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        assignmentTabHost.setup();

        //ViewPager is a layout manager that lets you flip right and left through tabs
        assignmentViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        /* Creating the assignment toolbar (actionbar).*/
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ImageButton add = (ImageButton)rootView.findViewById(R.id.add_assignment_button);
        ImageButton delete = (ImageButton)rootView.findViewById(R.id.delete_assignment_button);
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
                case R.id.add_assignment_button:
                    startActivity(new Intent(getActivity(), AddClassActivity.class));
                    break;
                case R.id.delete_assignment_button:
                    Log.d("on click", assignment);
                    ContentResolver cr = getActivity().getContentResolver();
                    /*need to write database*/
                    cr.delete(AssignmentContentProvider.CONTENT_URI,
                            AssignmentInfo.FeedEntry.COLUMN_NAME + "=?",
                            new String[]{assignment});
                    break;
            }
        }
    }



    /**
     * Helper method for updateChildFragments
     * @param viewId
     * @param position
     * @return
     */
    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }


    /**
     * Updates data on the current screen
     */
    private void fillData() {
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AssignmentInfo.FeedEntry.COLUMN_NAME} ;
        // Fields on the UI to which we map
        int[] to = new int[] { android.R.id.text1 };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.layout_title, null, from, to, 0);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
    }


    /**
     * Creates a new loader after the initLoader () call
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { AssignmentInfo.FeedEntry._ID, AssignmentInfo.FeedEntry.COLUMN_NAME};
        CursorLoader cursor_ld = new CursorLoader(getContext(),
                AssignmentContentProvider.CONTENT_URI, projection, null, null, null);
        return cursor_ld;
    }


    /**
     * Swaps the data in the adapter when loader is finished.
     * @param loader
     * @param data
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    /**
     * Defines what happens when loader is reset
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }


    /**
     * Defines what happens when fragment resumes running
     */
    @Override
    public void onResume(){
        super.onResume();
        /**
         * ToDo: Fix Loader issues. Right now, something is going wrong (probably in
         * ContentProvider) and the fragment does not get automatically updated with
         * new information. restartLoader() is a hotfix that forces the fragment to
         * destroy and recreate the loader and refresh the information, but this is
         * inefficient/bad since it is completely unnecessary to restartLoader on every
         * single onResume() call
         */
        getLoaderManager().restartLoader(0, null, this);
        Log.d("onResume", "string is " + assignment);
    }
}
