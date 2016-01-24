package io.github.cse110w260t13.ucsdcse110wi16.classplanner.navdrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.navdrawer.CoursePages.AssignmentsFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.navdrawer.CoursePages.ClassInfoFragment;

public class CoursesFragment extends Fragment{

    private TabHost courseTabHost;
    private ViewPager courseViewPager;
    private TabsAdapter courseTabAdapter;


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

        return rootView;
    }

    /* ViewPager requires an implementation of PagerAdapter to generate the pages that the
    view shows so TabsAdapter extends FragmentPagerAdapter.
    It listens to changes to the tabs.
    Then it changes pages accordingly.
    */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
        private final FragmentActivity mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo
        {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args)
            {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        /*Supplies dummy view that is shown as the tab content. TabHost has a
        * simplistic way of supplying the view/intent that each tab will show and
        * its not sufficient for switching between pages. So we can get around that by
        * setting the tabhost view to size 0 (invisible) and create our own view.*/
        static class DummyTabFactory implements TabHost.TabContentFactory
        {
            private final Context mContext;

            public DummyTabFactory(Context context)
            {
                mContext = context;
            }

            public View createTabContent(String tag)
            {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        //Constructor for TabsAdapter
        public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                           ViewPager pager, FragmentManager mng)
        {
            super(mng);
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.addOnPageChangeListener(this);//changed from original
        }

        //adds a Tab
        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
        {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return mTabs.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            TabInfo info = mTabs.get(position);

            return android.support.v4.app.Fragment.instantiate(mContext, info.clss.getName(), info.args);

        }

        public void onTabChanged(String tabId)
        {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
        }

        public void onPageSelected(int position)
        {
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

        public void onPageScrollStateChanged(int state)
        {
        }


    }





}
