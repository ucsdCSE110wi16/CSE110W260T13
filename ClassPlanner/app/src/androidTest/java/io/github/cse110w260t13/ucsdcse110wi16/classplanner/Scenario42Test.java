package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.os.SystemClock;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

/**
 * This intends to test scenario 4.2
 * https://github.com/ucsdCSE110wi16/CSE110W260T13/blob/master/documents/SCENARIOS.txt#L78
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class Scenario42Test {

    private static final String COURSE_NAME =  "math 217";
    private static final String ASSIGNMENT_NAME =  "Read about Hawking Radiation";
    private static final String ASSIGNMENT_TYPE =  "Intense";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Navigates to assignments fragment
     */
    @Before
    public void navigateToAssignmentsFragment() {
        onView(ViewMatchers.withContentDescription("Navigate up")).perform(click());
        onView(withText("Assignments")).perform(click());
        SystemClock.sleep(1000);
    }

    /**
     * Adds an assignment and makes sure that it is added
     *
     */
    @Test
    public void testAddAssignment() {
        onView(withId(R.id.add_button)).perform(click());
        SystemClock.sleep(1000);
        onView(ViewMatchers.withId(R.id.text_coursename))
                .perform(typeText(COURSE_NAME), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.edit_name))
                .perform(typeText(ASSIGNMENT_NAME), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.edit_type))
                .perform(typeText(ASSIGNMENT_TYPE), closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());
        onView(withText(ASSIGNMENT_NAME)).check(matches(isDisplayed()));
    }
}
