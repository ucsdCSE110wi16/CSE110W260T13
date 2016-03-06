package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.os.SystemClock;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;

/**
 * This intends to test scenario 6.1
 * https://github.com/ucsdCSE110wi16/CSE110W260T13/blob/master/documents/SCENARIOS.txt#L105
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ToDoFragTest {

    private static final String TEST_STRING =  "Write more Espresso tests";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Navigates to to-do fragment
     */
    @Before
    public void navigateToToDoFragment() {
        onView(ViewMatchers.withContentDescription("Navigate up")).perform(click());
        onView(withText("To-Do List")).perform(click());
        SystemClock.sleep(1000);
    }

    /**
     * Adds a to-do item and makes sure that it is added
     *
     */
    @Test
    public void testAddToDoItem() {
        onView(withId(R.id.AddToDoItem)).perform(click());
        SystemClock.sleep(1000);
        onView(ViewMatchers.withClassName(endsWith("EditText")))
                .perform(typeText(TEST_STRING), closeSoftKeyboard());
        onView(withText("Add")).perform(click());
        onView(withText(TEST_STRING)).check(matches(isDisplayed()));
    }

}
