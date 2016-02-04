package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.NavDrawerItem;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;

/**
 * This intends to test scenario #1.
 * See https://github.com/ucsdCSE110wi16/CSE110W260T13/blob/58f6644eea8052e753a35745e9aa098d3180ffed/SCENARIOS.txt#L9
 * for scenario.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationMenuTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Opens the navigation drawer to make it accessible to other tests
     */
    @Before
    public void openNavigationDrawer() {
        DrawerActions.open();
    }

    /**
     * Test navigation to HomeFragment
     *
     */
    @Test
    public void navigateToHomeFragment() {

    }

    /**
     * Test navigation to CalendarFragment
     *
     */
    @Test
    public void navigateToCalendarFragment() {

    }

    /**
     * Test navigation to CoursesFragment
     *
     */
    @Test
    public void navigateToCoursesFragment() {

    }

    /**
     * Test navigation to AssignmentsFragment
     *
     */
    @Test
    public void navigateToAssignmentsFragment() {

    }

    /**
     * Test navigation to ToDoListFragment
     *
     */
    @Test
    public void navigateToToDoListFragment() {

    }

}