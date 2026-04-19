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
        composeTestRule
            .onNodeWithText("Add")
            .performClick()
    }

    @Test
    fun testAddScreenTitleFieldIsDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Title *")
            .assertIsDisplayed()
    }

    @Test
    fun testAddScreenDescriptionFieldIsDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Description")
            .assertIsDisplayed()
    }

    @Test
    fun testSaveButtonIsDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Save Note")
            .assertIsDisplayed()
    }

    @Test
    fun testDiscardButtonIsDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Discard Changes")
            .assertIsDisplayed()
    }

    @Test
    fun testEmptyTitleShowsValidationError() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Save Note")
            .performClick()
        composeTestRule
            .onNodeWithText("Title cannot be empty")
            .assertIsDisplayed()
    }

    @Test
    fun testValidNoteSavesAndNavigatesBack() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("Valid Note Title")
        composeTestRule
            .onNodeWithText("Save Note")
            .performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testDiscardNavigatesBackWithoutSaving() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("Unsaved Note")
        composeTestRule
            .onNodeWithText("Discard Changes")
            .performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testCategoryChipsDisplayedOnAddScreen() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Work")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Study")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Personal")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Ideas")
            .assertIsDisplayed()
    }

    @Test
    fun testTitleCharacterCounterDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("0/100")
            .assertIsDisplayed()
    }

    @Test
    fun testDescriptionCharacterCounterDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("0/500")
            .assertIsDisplayed()
    }

    @Test
    fun testEditModePrePopulatesFields() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("Pre-filled Title")
        composeTestRule
            .onNodeWithText("Write the full note content here...")
            .performTextInput("Pre-filled Description")
        composeTestRule
            .onNodeWithText("Save Note")
            .performClick()
        composeTestRule
            .onNodeWithText("Pre-filled Title")
            .performClick()
        composeTestRule
            .onNodeWithText("Edit Note")
            .performClick()
        composeTestRule
            .onNodeWithText("Pre-filled Title")
            .assertIsDisplayed()
    }

    @Test
    fun testTemplateChipsDisplayed() {
        navigateToAddScreen()
        composeTestRule
            .onNodeWithText("Templates")
            .assertIsDisplayed()
    }
}