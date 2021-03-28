package com.example.byitscover

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.byitscover.helpers.CurrentBook
import com.example.byitscover.helpers.ScraperConstants
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewPageTest {

    @Test
    fun testFirstFragmentElementsNotNull() {
        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<ReviewPage>(fragmentArgs)

        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.cover)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.titleText)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.authorText)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.averageRatingText)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage1)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage2)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage3)))
        assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage4)))
    }
}