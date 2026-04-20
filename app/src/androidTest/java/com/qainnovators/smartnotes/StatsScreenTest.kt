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
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule
            .onNodeWithText("Statistics")
            .performClick()
    }

    private fun addNote(title: String, description: String = "Test description") {
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput(title)
        composeTestRule
            .onNodeWithText("Write the full note content here...")
            .performTextInput(description)
        composeTestRule.onNodeWithText("Save Note").performClick()
    }

    @Test
    fun testStatsScreenOpensFromMenu() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Notes Statistics")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsOverviewSection() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Overview")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsTotalNotes() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Total Notes")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsTotalWords() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Total Words")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsPinnedCount() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Pinned")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsFavouritesCount() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Favourites")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsCategorySection() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Notes by Category")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsRecentlyEditedSection() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Recently Edited")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsScreenShowsTopCategory() {
        navigateToStats()
        composeTestRule
            .onNodeWithText("Top Category")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsNoteCountUpdatesAfterAddingNote() {
        addNote("Stats Test Note")
        navigateToStats()
        composeTestRule
            .onNodeWithText("1")
            .assertIsDisplayed()
    }

    @Test
    fun testStatsBackButtonReturnsToList() {
        navigateToStats()
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }
}