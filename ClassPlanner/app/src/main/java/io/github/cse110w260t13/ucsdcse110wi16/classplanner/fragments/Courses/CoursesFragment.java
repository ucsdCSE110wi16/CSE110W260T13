package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.support.v7.app.AlertDialog;
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
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.AddClassActivity;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.AssignmentsFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.ClassInfoFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale.GradescaleFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.ErrorDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.TabsAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class CoursesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int REQUEST_CODE = 1;

    private View rootView;
    private TabHost courseTabHost;
    private ViewPager courseViewPager;
    private TabsAdapter courseTabAdapter;

    private SimpleCursorAdapter adapter;
    private Spinner spin;
    private String currentClass;

    public CoursesFragment(){}

    /**-------------------------------------------------------------------------------------------
     * onCreateView
     *-------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        /***********************TABS-RELATED INITIALIZATION AND SETTING****************************/
        //TabHost is the container for a tabbed window view
        courseTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        courseTabHost.setup();

        //ViewPager is a layout manager that lets you flip right and left through tabs
        courseViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        courseTabAdapter = new TabsAdapter(getActivity(), courseTabHost, courseViewPager,
                getChildFragmentManager());

        //Load Content of Each Tab
        courseTabAdapter.addTab(courseTabHost.newTabSpec("one").setIndicator("Class Info"),
                ClassInfoFragment.class, null);
        courseTabAdapter.addTab(courseTabHost.newTabSpec("two").setIndicator("Assignment"),
                AssignmentsFragment.class, null);
        courseTabAdapter.addTab(courseTabHost.newTabSpec("three").setIndicator("Scale"),
                GradescaleFragment.class, null);

        //Set the background and color of the tabs
        for (int i = 0; i < courseTabHost.getTabWidget().getChildCount(); i++) {
            View v = courseTabHost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_indicator_ab_example);
            TextView tv = (TextView) courseTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
            tv.setTextSize(12);
            Typeface face= Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway.ttf");
            tv.setTypeface(face);
        }

        /*************************TOOLBAR INITIALIZATION AND SETTING*******************************/
        //Create and set the toolbar(Actionbar) for the Courses page
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*************************SPINNER INITIALIZATION AND SETTING*******************************/
        //initialize dropdown list spinner, populate it with data, and set its listener
        spin=(Spinner) rootView.findViewById(R.id.spinner_nav);
        fillData();
        dropdownHandler drpHandler = new dropdownHandler();
        spin.setOnItemSelectedListener(drpHandler);

        /*************************BUTTON INITIALIZATION AND SETTING********************************/
        //set the listeners for the buttons on the Courses page
        ImageButton add = (ImageButton)rootView.findViewById(R.id.add_course_button);
        ImageButton delete = (ImageButton)rootView.findViewById(R.id.delete_course_button);
        ClickHandler click_handler = new ClickHandler();
        add.setOnClickListener(click_handler);
        delete.setOnClickListener(click_handler);

        return rootView;
    }


    /**--------------------------------------------------------------------------------------------
     * spinner handler that will handle changes when a different item on the dropdown
     * list is selected.
     *--------------------------------------------------------------------------------------------*/
    private class dropdownHandler implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Cursor cursor = (Cursor) adapter.getItem(position);
            String course_name = cursor.getString(cursor.getColumnIndex
                    (CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME));
            currentClass = course_name;
            updateChildFragments(course_name);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    /**--------------------------------------------------------------------------------------------
     * click handler for all buttons in the Courses Page
     *--------------------------------------------------------------------------------------------*/
    private class ClickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_course_button:
                    Intent intent = new Intent(getContext(), AddClassActivity.class);
                    intent.putExtra("mode", "create");
                    startActivity(intent);
                    break;
                case R.id.delete_course_button:
                    if(currentClass == null){
                        ErrorDialogFragment error = new ErrorDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("errorMsg", "You currently have no classes.");
                        error.setArguments(args);
                        error.show(getFragmentManager(), "No Classes Error Popup");
                        break;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete this?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ContentResolver cr = getActivity().getContentResolver();
                                    cr.delete(CourseCalendarContentProvider.CONTENT_URI,
                                            CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME + "=?",
                                            new String[]{currentClass});

                                    cr.delete(CalendarContentProvider.CONTENT_URI,
                                            CalendarInfo.FeedEntry.EVENT_TITLE + "=?",
                                            new String[]{currentClass});

                                    Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                                            CourseCalendarInfo.GeneralInfo.ALL_COLUMNS,
                                            null, null, null);
                                    if(cursor!=null && cursor.getCount() == 0){
                                        updateChildFragments(null);
                                        currentClass=null;
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
            }
        }
    }

    /**---------------------------------------------------------------------------------------------
     * Updates the child fragments(CLASS INFO and ASSIGNMENT) using what the current dropdown
     * list is set to
     * @param course_name
     *--------------------------------------------------------------------------------------------*/
    public void updateChildFragments(String course_name){
        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) courseViewPager.getAdapter();
        for(int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            Log.d("updateChildFragments", "testing " + course_name);
            String name = makeFragmentName(courseViewPager.getId(), i);
            Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment instanceof ClassInfoFragment) {
                ((ClassInfoFragment)viewPagerFragment).selectClass(course_name);
            }
            else if (viewPagerFragment instanceof  AssignmentsFragment){
                ((AssignmentsFragment)viewPagerFragment).test(course_name);
            }
            else if (viewPagerFragment instanceof GradescaleFragment){
                ((GradescaleFragment)viewPagerFragment).updateChart(course_name);
            }
        }
    }

    /**--------------------------------------------------------------------------------------------
     * Helper method for updateChildFragments
     *--------------------------------------------------------------------------------------------*/
    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    /**--------------------------------------------------------------------------------------------
     * Updates data on the current screen
     *--------------------------------------------------------------------------------------------*/
    private void fillData() {
        // Must include the _id column for the adapter to work
        String[] from = new String[] { CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME} ;
        // Fields on the UI to which we map
        int[] to = new int[] { android.R.id.text1 };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getContext(), R.layout.layout_title, null, from, to, 0);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spin.setAdapter(adapter);
    }

    /**-------------------------------------------------------------------------------------------
     * Defines what happens onActivityResult (when a class is done being edited, added, canceled)
     *-------------------------------------------------------------------------------------------*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            currentClass = data.getStringExtra("newName");
            updateChildFragments(currentClass);
        }
    }

    /**--------------------------------------------------------------------------------------------
     * THIS SEGMENT CONTAINS THE LOADER INTERFACE IMPLEMENTATION
     *--------------------------------------------------------------------------------------------*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { CourseCalendarInfo.GeneralInfo._ID,
                CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME};
        return new CursorLoader(getContext(), CourseCalendarContentProvider.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    /**--------------------------------------------------------------------------------------------
     * Defines what happens when fragment resumes running
     *--------------------------------------------------------------------------------------------*/
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
    }
}
