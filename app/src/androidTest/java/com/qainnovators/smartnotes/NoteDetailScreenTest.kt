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

    private fun addAndOpenNote(title: String, description: String = "Test description") {
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput(title)
        composeTestRule
            .onNodeWithText("Write the full note content here...")
            .performTextInput(description)
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText(title).performClick()
    }

    @Test
    fun testNoteDetailScreenDisplaysTitle() {
        addAndOpenNote("Detail Test Note")
        composeTestRule
            .onNodeWithText("Detail Test Note")
            .assertIsDisplayed()
    }

    @Test
    fun testNoteDetailScreenDisplaysDescription() {
        addAndOpenNote("Note With Desc", "This is the description")
        composeTestRule
            .onNodeWithText("This is the description")
            .assertIsDisplayed()
    }

    @Test
    fun testEditButtonNavigatesToEditScreen() {
        addAndOpenNote("Editable Note")
        composeTestRule.onNodeWithText("Edit Note").performClick()
        composeTestRule.onNodeWithText("Edit Note").assertIsDisplayed()
    }

    @Test
    fun testDeleteButtonShowsConfirmationDialog() {
        addAndOpenNote("Delete Me")
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule
            .onNodeWithText(
                "Are you sure you want to delete \"Delete Me\"? This cannot be undone."
            )
            .assertIsDisplayed()
    }

    @Test
    fun testCancelDeleteKeepsNote() {
        addAndOpenNote("Keep This Note")
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Keep This Note").assertIsDisplayed()
    }

    @Test
    fun testCategoryBadgeDisplayedOnDetailScreen() {
        addAndOpenNote("Category Test Note")
        composeTestRule
            .onNodeWithText("Personal")
            .assertIsDisplayed()
    }

    @Test
    fun testCreatedTimestampDisplayed() {
        addAndOpenNote("Timestamp Note")
        composeTestRule
            .onNodeWithText("Created")
            .assertIsDisplayed()
    }

    @Test
    fun testLastEditedTimestampDisplayed() {
        addAndOpenNote("Edited Note")
        composeTestRule
            .onNodeWithText("Last edited")
            .assertIsDisplayed()
    }

    @Test
    fun testExportButtonDisplayed() {
        addAndOpenNote("Export Note")
        composeTestRule
            .onNodeWithText("Export as TXT")
            .assertIsDisplayed()
    }

    @Test
    fun testConfirmDeleteNavigatesBackToList() {
        addAndOpenNote("To Be Deleted")
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testPinIndicatorShownWhenNotePinned() {
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("Pinned Note")
        composeTestRule
            .onNodeWithContentDescription("Pin")
            .performClick()
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText("Pinned Note").performClick()
        composeTestRule
            .onNodeWithContentDescription("Pinned")
            .assertIsDisplayed()
    }

    @Test
    fun testFavouriteIndicatorShownWhenNoteFavourited() {
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("Favourite Note")
        composeTestRule
            .onNodeWithContentDescription("Favourite")
            .performClick()
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText("Favourite Note").performClick()
        composeTestRule
            .onNodeWithContentDescription("Favourite")
            .assertIsDisplayed()
    }

    @Test
    fun testWordCountDisplayed() {
        addAndOpenNote("Stats Note", "Hello world this is a test")
        composeTestRule.onNodeWithText("words").assertIsDisplayed()
    }

    @Test
    fun testCharCountDisplayed() {
        addAndOpenNote("Stats Note 2", "Some content here")
        composeTestRule.onNodeWithText("chars").assertIsDisplayed()
    }

    @Test
    fun testReadTimeDisplayed() {
        addAndOpenNote("Stats Note 3", "Reading time test")
        composeTestRule.onNodeWithText("read time").assertIsDisplayed()
    }

    @Test
    fun test3DotMenuOpensOnDetailScreen() {
        addAndOpenNote("Menu Test Note")
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule.onNodeWithText("Share").assertIsDisplayed()
        composeTestRule.onNodeWithText("Copy text").assertIsDisplayed()
    }
}