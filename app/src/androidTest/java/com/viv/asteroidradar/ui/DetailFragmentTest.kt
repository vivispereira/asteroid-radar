package com.viv.asteroidradar.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.viv.asteroidradar.R
import com.viv.asteroidradar.presentation.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Espresso UI tests for DetailFragment
 */
@RunWith(AndroidJUnit4::class)
class DetailFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private var mockBaseUrl: String = ""

    @Before
    fun setUp() {
        mockBaseUrl = MockApiHelper.startMockServer()
        MockApiHelper.enqueueMockAsteroidResponse()
        MockApiHelper.enqueueMockPictureOfDayResponse()
    }

    @After
    fun tearDown() {
        MockApiHelper.stopMockServer()
    }

    /**
     * Navigate to detail screen by clicking an asteroid
     */
    private fun navigateToDetailScreen() {
        Thread.sleep(500)
        onView(withId(R.id.asteroid_recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        Thread.sleep(300)
    }

    /**
     * Test that the detail screen displays the asteroid image
     */
    @Test
    fun detailFragment_AsteroidImageIsVisible() {
        navigateToDetailScreen()

        onView(withId(R.id.asteroid_image))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays close approach data title
     */
    @Test
    fun detailFragment_CloseApproachDataTitleIsVisible() {
        navigateToDetailScreen()

        onView(withText(R.string.close_approach_data_title))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays the close approach date
     */
    @Test
    fun detailFragment_CloseApproachDateIsVisible() {
        navigateToDetailScreen()

        onView(withId(R.id.close_approach_date))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays absolute magnitude title
     */
    @Test
    fun detailFragment_AbsoluteMagnitudeTitleIsVisible() {
        navigateToDetailScreen()

        onView(withText(R.string.absolute_magnitude_title))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays estimated diameter section
     */
    @Test
    fun detailFragment_EstimatedDiameterSectionIsVisible() {
        navigateToDetailScreen()

        onView(withText(R.string.estimated_diameter_title))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays relative velocity section
     */
    @Test
    fun detailFragment_RelativeVelocitySectionIsVisible() {
        navigateToDetailScreen()

        onView(withText(R.string.relative_velocity_title))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the detail screen displays distance from earth section
     */
    @Test
    fun detailFragment_DistanceFromEarthSectionIsVisible() {
        navigateToDetailScreen()

        onView(withText(R.string.distance_from_earth_title))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the help button is visible on the detail screen
     */
    @Test
    fun detailFragment_HelpButtonIsVisible() {
        navigateToDetailScreen()

        onView(withId(R.id.help_button))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that clicking the help button opens a dialog
     */
    @Test
    fun detailFragment_ClickHelpButton_DisplaysDialog() {
        navigateToDetailScreen()

        // Click the help button
        onView(withId(R.id.help_button))
            .perform(click())

        // Verify the dialog text is displayed
        onView(withText(R.string.astronomica_unit_explanation))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the dialog can be dismissed
     */
    @Test
    fun detailFragment_HelpDialog_CanBeDismissed() {
        Thread.sleep(1000)
        navigateToDetailScreen()

        // Click the help button to open dialog
        onView(withId(R.id.help_button))
            .perform(click())

        // Verify dialog is shown
        onView(withText(R.string.astronomica_unit_explanation))
            .check(matches(isDisplayed()))

        // Click OK to dismiss
        onView(withText(android.R.string.ok))
            .perform(click())

        // Verify dialog is gone by checking the help button is still visible
        onView(withId(R.id.help_button))
            .check(matches(isDisplayed()))
    }

    /**
     * Test that the toolbar title is set correctly
     */
    @Test
    fun detailFragment_ToolbarTitleIsSet() {
        navigateToDetailScreen()

        // The detail screen should have a title in the action bar
        onView(
            allOf(
                withText(R.string.detail_title),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
    }

    /**
     * Test that the back button is visible on detail screen
     */
    @Test
    fun detailFragment_BackButtonIsVisible() {
        navigateToDetailScreen()

        // Check for the up/back button
        onView(
            allOf(
                withContentDescription(org.hamcrest.Matchers.containsString("Navigate up")),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
    }

    /**
     * Test that multiple detail screens can be viewed sequentially
     */
    @Test
    fun detailFragment_CanViewMultipleAsteroidsSequentially() {
        // View first asteroid
        navigateToDetailScreen()
        onView(withId(R.id.close_approach_date))
            .check(matches(isDisplayed()))

        // Go back
        Thread.sleep(500)
        onView(
            allOf(
                withContentDescription(org.hamcrest.Matchers.containsString("Navigate up")),
                isDisplayed()
            )
        ).perform(click())

        Thread.sleep(300)

        // Scroll and click second asteroid
        onView(withId(R.id.asteroid_recycler))
            .perform(
                RecyclerViewActions.scrollToPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                    1
                )
            )
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        Thread.sleep(300)

        // Verify we're on the detail screen
        onView(withId(R.id.close_approach_date))
            .check(matches(isDisplayed()))
    }
}
