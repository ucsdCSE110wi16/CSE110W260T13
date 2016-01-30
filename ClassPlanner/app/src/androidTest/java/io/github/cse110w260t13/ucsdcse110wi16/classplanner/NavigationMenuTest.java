package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by nick on 1/30/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationMenuTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void navigateToHomeFragment() {
        // Test navigation to HomeFragment
    }

    @Test
    public void navigateToCalendarFragment() {
        // Test navigation to CalendarFragment
    }

    @Test
    public void navigateToCoursesFragment() {
        // Test navigation to CoursesFragment
    }

    @Test
    public void navigateToAssignmentsFragment() {
        // Test navigation to AssignmentsFragment
    }

    @Test
    public void navigateToToDoListFragment() {
        // Test navigation to ToDoListFragment
    }

}
