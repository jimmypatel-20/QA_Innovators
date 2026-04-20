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

    private fun navigateToAddScreen() {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.waitForIdle()
    }

    private fun fillAndSaveNote(title: String, description: String = "Test description") {
        // Find the first node with SetTextAction (the title field)
        composeTestRule
            .onAllNodes(hasSetTextAction())
            .onFirst()
            .performTextInput(title)
        composeTestRule
            .onAllNodes(hasSetTextAction())
            .onLast()
            .performTextInput(description)
        composeTestRule.onNodeWithText("Save Note").performClick()
        composeTestRule.waitForIdle()
    }

    private fun addAndOpenNote(title: String, description: String = "Test description") {
        navigateToAddScreen()
        fillAndSaveNote(title, description)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText(title)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText(title).performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Edit Note")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun testNoteDetailScreenDisplaysTitle() {
        addAndOpenNote("Detail Test Note")
        composeTestRule.onNodeWithText("Detail Test Note").assertIsDisplayed()
    }

    @Test
    fun testEditButtonIsDisplayed() {
        addAndOpenNote("Editable Note")
        composeTestRule.onNodeWithText("Edit Note").assertIsDisplayed()
    }

    @Test
    fun testDeleteButtonIsDisplayed() {
        addAndOpenNote("Delete Me")
        composeTestRule.onNodeWithText("Delete Note").assertIsDisplayed()
    }

    @Test
    fun testCancelDeleteKeepsNote() {
        addAndOpenNote("Keep This Note")
        composeTestRule.onNodeWithText("Delete Note").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Keep This Note").assertIsDisplayed()
    }
}