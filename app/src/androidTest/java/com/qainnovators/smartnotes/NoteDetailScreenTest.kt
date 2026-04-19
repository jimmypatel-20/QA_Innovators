package com.qainnovators.smartnotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun addTestNote(title: String, description: String = "Test description") {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("Enter a short, clear title...").performTextInput(title)
        composeTestRule.onNodeWithText("Write the full note content here...").performTextInput(description)
        composeTestRule.onNodeWithText("Save Note").performClick()
    }

    @Test
    fun testNoteDetailScreenDisplaysTitle() {
        addTestNote("Detail Test Note")
        composeTestRule.onNodeWithText("Detail Test Note").performClick()
        composeTestRule.onNodeWithText("Detail Test Note").assertIsDisplayed()
    }

    @Test
    fun testNoteDetailScreenDisplaysDescription() {
        addTestNote("Note With Desc", "This is the description")
        composeTestRule.onNodeWithText("Note With Desc").performClick()
        composeTestRule.onNodeWithText("This is the description").assertIsDisplayed()
    }

    @Test
    fun testEditButtonNavigatesToEditScreen() {
        addTestNote("Editable Note")
        composeTestRule.onNodeWithText("Editable Note").performClick()
        composeTestRule.onNodeWithText("Edit Note").performClick()
        composeTestRule.onNodeWithText("Edit Note").assertIsDisplayed()
    }

    @Test
    fun testDeleteButtonShowsConfirmationDialog() {
        addTestNote("Delete Me")
        composeTestRule.onNodeWithText("Delete Me").performClick()
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Are you sure you want to delete \"Delete Me\"? This cannot be undone.")
            .assertIsDisplayed()
    }

    @Test
    fun testCancelDeleteKeepsNote() {
        addTestNote("Keep This Note")
        composeTestRule.onNodeWithText("Keep This Note").performClick()
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Keep This Note").assertIsDisplayed()
    }
}
