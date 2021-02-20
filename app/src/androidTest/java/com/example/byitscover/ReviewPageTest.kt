package com.example.byitscover

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewPageTest {

    @Test
    fun testFirstFragmentElementsNotNull() {
        val fragmentArgs = Bundle()
        val scenario = launchFragment<FirstFragment>(fragmentArgs)

        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.cover)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.titleText)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.authorText)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.averageRatingText)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage1)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage2)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage3)))
        Assert.assertNotNull(Espresso.onView(ViewMatchers.withId(R.id.revImage4)))
    }
}