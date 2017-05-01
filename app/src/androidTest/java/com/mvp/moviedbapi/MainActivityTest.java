package com.mvp.moviedbapi;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.mvp.moviedbapi.activities.MainActivity;
import com.mvp.moviedbapi.base.AbstractTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends AbstractTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testMainActivityMovieSearch() throws InterruptedException {
        // No text
        onView(withId(R.id.main_activity_search_button)).perform(click());
        onView(withText(R.string.search_error_no_text)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Type text and then press the button.
        onView(withId(R.id.main_activity_edit_text))
                .perform(typeText("star wars"), closeSoftKeyboard());
        onView(withId(R.id.main_activity_search_button)).perform(click());

        // Check that list adapter is set and views populated
        RecyclerView recyclerView = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.main_activity_recycler_view);
        //One improvement would be not to rely on the real network query, but mock the response (Mockito etc...) to avoid depending on network related stuff.
        waitForCondition(() -> recyclerView != null && recyclerView.getAdapter() != null, 3000);
        assertNotNull(recyclerView);
        assertNotNull(recyclerView.getAdapter());
        assertNotSame(0, recyclerView.getAdapter().getItemCount());
    }

    //TODO test for next page (did not have the time)

}
