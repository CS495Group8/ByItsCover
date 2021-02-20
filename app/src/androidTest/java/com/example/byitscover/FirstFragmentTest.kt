package com.example.byitscover

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.Assert.assertNotNull


@RunWith(AndroidJUnit4::class)
class FirstFragmentTest {

    @Test
    fun testFirstFragmentElementsNotNull() {
        val fragmentArgs = Bundle()
        val scenario = launchFragment<FirstFragment>(fragmentArgs)

        assertNotNull(onView(withId(R.id.mainTitleText)))
        assertNotNull(onView(withId(R.id.searchByCoverButton)))
        assertNotNull(onView(withId(R.id.searchByTitleButton)))
    }
}