package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

/* IMPLEMENTATION OF ADAPTER THAT IS NECESSARY FOR TABS TO WORK CORRECTLY
ViewPager requires an implementation of PagerAdapter to generate the pages that the
view shows so TabsAdapter extends FragmentPagerAdapter.
It listens to changes to the tabs.
Then it changes pages accordingly.
*/
public class TabsAdapter extends FragmentPagerAdapter
        implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private final FragmentActivity mContext;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();


    /**
     * Class that will contain the information of the tab that it is
     * created for
     */
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


    /**
     * Constructor for a TabsAdapter
      * @param activity
     * @param tabHost
     * @param pager
     * @param mng
     */
    public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                       ViewPager pager, FragmentManager mng) {
        super(mng);
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
    }


    /**
     * Adds a tab to the page using the tabsadapter
      * @param tabSpec
     * @param clss
     * @param args
     */
    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }


    /**
     * Returns the number of tabs
     * @return
     */
    @Override
    public int getCount() {
        return mTabs.size();
    }


    /**
     * Returns the position that the tab is at.
     * @param position
     * @return
     */
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return android.support.v4.app.Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }


    /**
     * Changes the shown item based on the current tab
     * @param tabId
     */
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    /**
     * Attempts to prevent TabHost from pulling the focus out of the ViewPager
     * @param position
     */
    public void onPageSelected(int position) {
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }


    public void onPageScrollStateChanged(int state) {
    }
}

