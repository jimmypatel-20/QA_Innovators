package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testTopAppBarDisplayed() {
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testFabIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Add Note")
            .assertIsDisplayed()
    }

    @Test
    fun testSearchBarIsDisplayed() {
        composeTestRule
            .onNodeWithText("Search notes...")
            .assertIsDisplayed()
    }

    @Test
    fun testCategoryChipsAreDisplayed() {
        // Use onFirst() because "Personal" may appear in note cards too
        composeTestRule.onAllNodesWithText("All").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Work").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Personal").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Study").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Ideas").onFirst().assertIsDisplayed()
    }

    @Test
    fun testSyncedStatusDisplayed() {
        composeTestRule
            .onNodeWithText("Synced just now")
            .assertIsDisplayed()
    }

    @Test
    fun testMoreOptionsMenuOpens() {
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Sort by")
            .assertIsDisplayed()
    }
}