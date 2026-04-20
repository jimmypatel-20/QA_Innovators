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
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule
            .onNodeWithText("Trash")
            .performClick()
    }

    private fun addAndDeleteNote(title: String) {
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput(title)
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.onNodeWithText(title).performClick()
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.onNodeWithText("Move to Trash").performClick()
    }

    @Test
    fun testTrashScreenOpensFromMenu() {
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Trash")
            .assertIsDisplayed()
    }

    @Test
    fun testTrashScreenShowsEmptyState() {
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Trash is empty")
            .assertIsDisplayed()
    }

    @Test
    fun testTrashScreenShowsEmptyDescription() {
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Deleted notes appear here")
            .assertIsDisplayed()
    }

    @Test
    fun testDeletedNoteAppearsInTrash() {
        addAndDeleteNote("Trash Test Note")
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Trash Test Note")
            .assertIsDisplayed()
    }

    @Test
    fun testRestoreButtonDisplayedOnTrashedNote() {
        addAndDeleteNote("Restore Me")
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Restore")
            .assertIsDisplayed()
    }

    @Test
    fun testDeleteButtonDisplayedOnTrashedNote() {
        addAndDeleteNote("Delete Forever")
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Delete")
            .assertIsDisplayed()
    }

    @Test
    fun testRestoreNoteReturnsToList() {
        addAndDeleteNote("Restore This")
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Restore")
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        composeTestRule
            .onNodeWithText("Restore This")
            .assertIsDisplayed()
    }

    @Test
    fun testPermanentDeleteRemovesFromTrash() {
        addAndDeleteNote("Permanent Delete")
        navigateToTrash()
        composeTestRule
            .onNodeWithText("Delete")
            .performClick()
        composeTestRule
            .onNodeWithText("Trash is empty")
            .assertIsDisplayed()
    }

    @Test
    fun testClearTrashButtonAppearsWhenNotEmpty() {
        addAndDeleteNote("Clear Trash Test")
        navigateToTrash()
        composeTestRule
            .onNodeWithContentDescription("Clear trash")
            .assertIsDisplayed()
    }

    @Test
    fun testInfoBannerDisplayedInTrash() {
        addAndDeleteNote("Banner Test")
        navigateToTrash()
        composeTestRule
            .onNodeWithText(
                "Notes in trash can be restored or permanently deleted"
            )
            .assertIsDisplayed()
    }

    @Test
    fun testTrashBackButtonReturnsToList() {
        navigateToTrash()
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }
}