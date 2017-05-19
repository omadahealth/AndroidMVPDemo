package com.mvp.moviedbapi

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mvp.moviedbapi.activities.MainActivity
import com.mvp.moviedbapi.base.AbstractTest
import com.mvp.moviedbapi.base.Condition
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNotSame
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : AbstractTest() {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    @Throws(InterruptedException::class)
    fun testMainActivityMovieSearch() {
        // No text
        onView(withId(R.id.searchButton)).perform(click())
        onView(withText(R.string.search_error_no_text)).inRoot(withDecorView(not<View>(`is`<View>(mActivityRule.activity.window.decorView))))
                .check(matches(isDisplayed()))

        // Type text and then press the button.
        onView(withId(R.id.edittext))
                .perform(typeText("star wars"), closeSoftKeyboard())
        onView(withId(R.id.searchButton)).perform(click())

        // Check that list adapter is set and views populated
        val recyclerView = mActivityRule.activity.findViewById(R.id.recyclerView) as RecyclerView
        //One improvement would be not to rely on the real network query, but mock the response (Mockito etc...) to avoid depending on network related stuff.
        waitForCondition(object : Condition {
            override val isSatisfied: Boolean
                get() = recyclerView.adapter != null
        }, 3000)
        assertNotNull(recyclerView)
        assertNotNull(recyclerView.adapter)
        assertNotSame(0, recyclerView.adapter.itemCount)
    }

    //TODO test for next page (did not have the time)

}
