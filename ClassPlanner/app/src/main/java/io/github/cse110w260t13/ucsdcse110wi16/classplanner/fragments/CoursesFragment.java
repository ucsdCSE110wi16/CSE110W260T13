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

public class CoursesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TabHost courseTabHost;
    private ViewPager courseViewPager;
    private TabsAdapter courseTabAdapter;

    private SimpleCursorAdapter adapter;
    private Spinner spin;
    private String currentClass;

    private SQLiteDatabase db;
    private CourseCalendarDbHelper mDbHelper;

    public CoursesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        //TabHost is the container for a tabbed window view
        courseTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        courseTabHost.setup();

        //ViewPager is a layout manager that lets you flip right and left through tabs
        courseViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        courseTabAdapter = new TabsAdapter(getActivity(), courseTabHost, courseViewPager, getChildFragmentManager());

        //Load Content of Each Tab
        courseTabAdapter.addTab(courseTabHost.newTabSpec("one").setIndicator("Class Info"), ClassInfoFragment.class, null);
        courseTabAdapter.addTab(courseTabHost.newTabSpec("two").setIndicator("Assignment"), AssignmentsFragment.class, null);

        /* Creating the courses_toolbar (actionbar) for the courses tab.*/
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        spin=(Spinner) rootView.findViewById(R.id.spinner_nav);
        /* Populate this spinner by calling fillData()*/
        fillData();
        dropdownHandler drpHandler = new dropdownHandler();
        spin.setOnItemSelectedListener(drpHandler);

        /* Programatically changing the text/design of tab widget because of issues changing the
        text & design via themes and styles
        TODO: Move this to be done through XML files */
        for (int i = 0; i < courseTabHost.getTabWidget().getChildCount(); i++) {
            View v = courseTabHost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_indicator_ab_example);
            TextView tv = (TextView) courseTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
            tv.setTextSize(12);
            Typeface face= Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway.ttf");
            tv.setTypeface(face);
        }

        ImageButton add = (ImageButton)rootView.findViewById(R.id.add_course_button);
        ImageButton delete = (ImageButton)rootView.findViewById(R.id.delete_course_button);
        clickHandler click_handler = new clickHandler();
        add.setOnClickListener(click_handler);
        delete.setOnClickListener(click_handler);

        return rootView;
    }


    /**
     * spinner handler that will handle changes when a different item on the dropdown
     * list is selected.
     */
    private class dropdownHandler implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Cursor cursor = (Cursor) adapter.getItem(position);
            String course_name = cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME));
            currentClass = course_name;

            Log.d("dropdwnHandler", "Current course_name is "+ currentClass);
            /*Iterate through all the fragments and update info*/
            updateChildFragments(course_name);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }


    /**
     * click handler for all buttons in the Courses Page
     */
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_course_button:
                    startActivity(new Intent(getActivity(), AddClassActivity.class));
                    break;
                case R.id.delete_course_button:
                    Log.d("on click", currentClass);
                    ContentResolver cr = getActivity().getContentResolver();
                    cr.delete(CourseCalendarContentProvider.CONTENT_URI,
                            CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                            new String[]{currentClass});
                    break;
            }
        }
    }


    /**
     * Updates the child fragments(CLASS INFO and ASSIGNMENT) using what the current dropdown
     * list is set to
     * @param course_name
     */
    private void updateChildFragments(String course_name){
        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) courseViewPager.getAdapter();
        for(int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            Log.d("updateChildFragments", "testing " + course_name);
            String name = makeFragmentName(courseViewPager.getId(), i);
            Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment instanceof ClassInfoFragment) {
                ((ClassInfoFragment)viewPagerFragment).selectClass(course_name);
            }
            if (viewPagerFragment instanceof  AssignmentsFragment){
                ((AssignmentsFragment)viewPagerFragment).test(course_name);
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
        String[] from = new String[] { CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME} ;
        // Fields on the UI to which we map
        int[] to = new int[] { android.R.id.text1 };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.layout_title, null, from, to, 0);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spin.setAdapter(adapter);
    }


    /**
     * Creates a new loader after the initLoader () call
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { CourseCalendarInfo.FeedEntry._ID, CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME};
        CursorLoader cursor_ld = new CursorLoader(getContext(),
                CourseCalendarContentProvider.CONTENT_URI, projection, null, null, null);
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
        Log.d("onResume", "string is " + currentClass);
        updateChildFragments(currentClass);
    }
}
