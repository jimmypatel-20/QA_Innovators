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
    fun testFabClickNavigatesToAddScreen() {
        composeTestRule
            .onNodeWithContentDescription("Add Note")
            .performClick()
        composeTestRule
            .onNodeWithText("New Note")
            .assertIsDisplayed()
    }

    @Test
    fun testEmptyStateMessageShownWhenNoNotes() {
        composeTestRule
            .onNodeWithText("No notes yet. Tap + to add one!")
            .assertIsDisplayed()
    }

    @Test
    fun testAddNoteAndVerifyItAppearsInList() {
        composeTestRule
            .onNodeWithContentDescription("Add Note")
            .performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performClick()
        composeTestRule
            .onNodeWithText("Enter a short, clear title...")
            .performTextInput("My First Note")
        composeTestRule
            .onNodeWithText("Save Note")
            .performClick()
        composeTestRule
            .onNodeWithText("My First Note")
            .assertIsDisplayed()
    }
}