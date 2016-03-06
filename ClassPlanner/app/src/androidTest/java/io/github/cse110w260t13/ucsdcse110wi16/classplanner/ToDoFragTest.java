package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.nav_drawer.MainActivity;

/**
 * Created by Bryan Yang on 3/5/2016.
 */
@RunWith(AndroidJUnit4.class)

public class ToDoFragTest {

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
}
