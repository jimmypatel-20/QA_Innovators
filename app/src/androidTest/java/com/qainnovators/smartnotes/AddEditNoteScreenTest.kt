package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddEditNoteScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun navigateToAddScreen() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testAddScreenTitleFieldIsDisplayed() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Title *").assertIsDisplayed()
    }

    @Test
    fun testAddScreenDescriptionFieldIsDisplayed() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun testSaveButtonIsDisplayed() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Save Note").assertIsDisplayed()
    }

    @Test
    fun testDiscardButtonIsDisplayed() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Discard Changes").assertIsDisplayed()
    }

    @Test
    fun testEmptyTitleShowsValidationError() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText("Title cannot be empty").assertIsDisplayed()
    }

    @Test
    fun testCategoryChipsDisplayedOnAddScreen() {
        navigateToAddScreen()
        composeTestRule.onNodeWithText("Work").assertIsDisplayed()
        composeTestRule.onNodeWithText("Study").assertIsDisplayed()
        composeTestRule.onNodeWithText("Personal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ideas").assertIsDisplayed()
    }
}