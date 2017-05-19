package com.mvp.moviedbapi.base

import junit.framework.Assert.fail

/**
 * Created by olivier.goutay on 4/18/17.
 */

open class AbstractTest {

    /**
     * Wait for the specific time using [Thread.sleep]

     * @param milliseconds The time we want to wait for in millis
     */
    protected fun waitFor(milliseconds: Int) {
        try {
            Thread.sleep(milliseconds.toLong())
        } catch (e: Exception) {
            fail()
        }

    }

    /**
     * Wait for a condition to be true

     * @param condition The condition we want to be true
     * *
     * @param time      The time to wait for
     */
    protected fun waitForCondition(condition: Condition, time: Int) {
        var timeWaited = 0
        while (timeWaited < time) {
            if (condition.isSatisfied) {
                return
            }
            waitFor(200)
            timeWaited += 200
        }
    }
}
