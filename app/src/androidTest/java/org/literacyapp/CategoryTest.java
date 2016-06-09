package org.literacyapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategoryTest {

    @Rule
    public ActivityTestRule<CategoryActivity> activityTestRule = new ActivityTestRule<>(CategoryActivity.class);

    @Test
    public void testSwipe() {
        onView(withId(R.id.categoryContainer))
                .perform(swipeLeft());

        onView(withId(R.id.categoryContainer))
                .perform(swipeLeft());

        onView(withId(R.id.categoryContainer))
                .perform(swipeLeft());

        onView(withId(R.id.categoryContainer))
                .perform(swipeRight());

        onView(withId(R.id.categoryContainer))
                .perform(swipeRight());

        onView(withId(R.id.categoryContainer))
                .perform(swipeRight());
    }
}
