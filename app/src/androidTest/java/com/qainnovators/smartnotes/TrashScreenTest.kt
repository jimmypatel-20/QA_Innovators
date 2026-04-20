package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrashScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun navigateToTrash() {
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Trash").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testTrashScreenOpensFromMenu() {
        navigateToTrash()
        composeTestRule.onNodeWithText("Trash").assertIsDisplayed()
    }

    @Test
    fun testTrashScreenShowsEmptyState() {
        navigateToTrash()
        composeTestRule.onNodeWithText("Trash is empty").assertIsDisplayed()
    }

    @Test
    fun testTrashBackButtonReturnsToList() {
        navigateToTrash()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Smart Notes Manager").assertIsDisplayed()
    }
}