package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;

/**
 * This intends to test scenario 1.1
 * See https://github.com/ucsdCSE110wi16/CSE110W260T13/blob/58f6644eea8052e753a35745e9aa098d3180ffed/SCENARIOS.txt#L9
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class Scenario11Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Opens the navigation drawer to make it accessible to other tests
     */
    @Before
    public void openNavigationDrawer() {
        onView(ViewMatchers.withContentDescription("Navigate up")).perform(click());
    }

    /**
     * Test navigation to CalendarFragment
     *
     */
    @Test
    public void navigateToCalendarFragment() {
        ViewInteraction fragmentContainer = onView(withId(R.id.fragment_calendar_coordinator_layout));
        fragmentContainer.check(ViewAssertions.doesNotExist());
        onView(withText("Calendar")).perform(click());
        fragmentContainer.check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test navigation to CoursesFragment
     *
     */
    @Test
    public void navigateToCoursesFragment() {
        ViewInteraction fragmentContainer = onView(withId(R.id.fragment_courses_linear_layout));
        fragmentContainer.check(ViewAssertions.doesNotExist());
        onView(withText("Courses")).perform(click());
        fragmentContainer.check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test navigation to AssignmentsFragment
     *
     */
    @Test
    public void navigateToAssignmentsFragment() {
        ViewInteraction fragmentContainer = onView(withId(R.id.fragment_assignment_linear_layout));
        fragmentContainer.check(ViewAssertions.doesNotExist());
        onView(withText("Assignments")).perform(click());
        fragmentContainer.check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test navigation to ToDoListFragment
     *
     */
    @Test
    public void navigateToToDoListFragment() {
        ViewInteraction fragmentContainer = onView(withId(R.id.fragment_todo_relative_layout));
        fragmentContainer.check(ViewAssertions.doesNotExist());
        onView(withText("To-Do List")).perform(click());
        fragmentContainer.check(ViewAssertions.matches(isDisplayed()));
    }

}
