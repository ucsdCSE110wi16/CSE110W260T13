package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.AssignmentsFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages.ClassInfoFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.AddClass;

public class CoursesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TabHost courseTabHost;
    private ViewPager courseViewPager;
    private TabsAdapter courseTabAdapter;

    private SimpleCursorAdapter adapter;
    private Spinner spin;


    public CoursesFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);

    }

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
        /*Spinner_nav is located inside R.id.courses_toolbar XML. So we create a spinner with this
         layout from the courses_toolbar and then upload the strings to the spinner as well as set
         the adapter to the spinner.
          */

        spin=(Spinner) rootView.findViewById(R.id.spinner_nav);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String[] projections={
                CourseCalendarInfo.FeedEntry._ID,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME
        };


        /*
        //Read data from database.
        CourseCalendarDbHelper mDbHelper = new CourseCalendarDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.query(
                CourseCalendarInfo.FeedEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );
        final int size = c.getCount();
        System.out.println(size);
        String[] items = new String[size];
        if (c.moveToFirst()) {
            int i = 0;
            do {
                items[i] = c.getString(c.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME));
                System.out.println(items[i]);
                i++;
            } while (c.moveToNext());
        }

        //String[] test = new String[]{"123", "456", "678"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.layout_title, items);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spin.setAdapter(adapter);*/

        fillData();

        //Changes the look of the tabs. (Was unable to change it successfully in the XML.
        for (int i = 0; i < courseTabHost.getTabWidget().getChildCount(); i++) {
            View v = courseTabHost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_indicator_ab_example);
            TextView tv = (TextView) courseTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
            tv.setTextSize(12);
            Typeface face= Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway.ttf");
            tv.setTypeface(face);
        }

        //Adds a FAB
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.courses_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddClass.class));
            }
        });


        return rootView;
    }

    /* ViewPager requires an implementation of PagerAdapter to generate the pages that the
    view shows so TabsAdapter extends FragmentPagerAdapter.
    It listens to changes to the tabs.
    Then it changes pages accordingly.
    */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final FragmentActivity mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        /*Supplies dummy view that is shown as the tab content. TabHost has a
        * simplistic way of supplying the view/intent that each tab will show and
        * its not sufficient for switching between pages. So we can get around that by
        * setting the tabhost view to size 0 (invisible) and create our own view.*/
        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        //Constructor for TabsAdapter
        public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                           ViewPager pager, FragmentManager mng) {
            super(mng);
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.addOnPageChangeListener(this);//changed from original
        }

        //adds a Tab
        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);

            return android.support.v4.app.Fragment.instantiate(mContext, info.clss.getName(), info.args);

        }

        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }
        private void fillData() {
            // Must include the _id column for the adapter to work
            String[] from = new String[] { CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME} ;
            // Fields on the UI to which we map
            int[] to = new int[] { android.R.id.text1 };

            getLoaderManager().initLoader(0, null, this);
            adapter = new SimpleCursorAdapter(getContext(), R.layout.layout_title, null, from, to, 0);
            adapter.setDropDownViewResource(R.layout.layout_drop_list);
            spin.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { CourseCalendarInfo.FeedEntry._ID, CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME};
        CursorLoader cursor_ld = new CursorLoader(getContext(),
                CourseCalendarContentProvider.CONTENT_URI, projection, null, null, null);
        return cursor_ld;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

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
