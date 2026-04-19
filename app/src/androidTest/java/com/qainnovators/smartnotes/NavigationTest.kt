package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppStartsOnNoteListScreen() {
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testNavigateToAddNoteScreen() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("New Note").assertIsDisplayed()
    }

    @Test
    fun testBackFromAddScreenReturnsToList() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Smart Notes Manager").assertIsDisplayed()
    }

    @Test
    fun testFullNavigationFlow() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("Enter a short, clear title...").performTextInput("Nav Test Note")
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText("Nav Test Note").performClick()
        composeTestRule.onNodeWithText("Note Details").assertIsDisplayed()
    }

    @Test
    fun testConfirmDeleteNavigatesBackToList() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("Enter a short, clear title...").performTextInput("To Be Deleted")
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText("To Be Deleted").performClick()
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithText("Smart Notes Manager").assertIsDisplayed()
    }
}