package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun navigateToStats() {
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Statistics").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testStatsScreenOpensFromMenu() {
        navigateToStats()
        composeTestRule.onNodeWithText("Notes Statistics").assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsOverviewSection() {
        navigateToStats()
        composeTestRule.onNodeWithText("Overview").assertIsDisplayed()
    }

    @Test
    fun testStatsBackButtonReturnsToList() {
        navigateToStats()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Smart Notes Manager").assertIsDisplayed()
    }
}
