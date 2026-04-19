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

    private fun addNote(title: String, description: String = "Test description") {
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("Enter a short, clear title...").performTextInput(title)
        composeTestRule.onNodeWithText("Write the full note content here...").performTextInput(description)
        composeTestRule.onNodeWithText("Save Note").performClick()
    }

    @Test
    fun testTopAppBarDisplayed() {
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testFabIsDisplayed() {
        composeTestRule
            .onNodeWithText("Add")
            .assertIsDisplayed()
    }

    @Test
    fun testFabClickNavigatesToAddScreen() {
        composeTestRule
            .onNodeWithText("Add")
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
        addNote("My First Note")
        composeTestRule
            .onNodeWithText("My First Note")
            .assertIsDisplayed()
    }

    @Test
    fun testSearchBarIsDisplayed() {
        composeTestRule
            .onNodeWithText("Search notes...")
            .assertIsDisplayed()
    }

    @Test
    fun testSearchFiltersNotes() {
        addNote("Android Development")
        addNote("Grocery List")
        composeTestRule
            .onNodeWithText("Search notes...")
            .performTextInput("Android")
        composeTestRule
            .onNodeWithText("Android Development")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Grocery List")
            .assertDoesNotExist()
    }

    @Test
    fun testCategoryChipsAreDisplayed() {
        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Work").assertIsDisplayed()
        composeTestRule.onNodeWithText("Study").assertIsDisplayed()
        composeTestRule.onNodeWithText("Personal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ideas").assertIsDisplayed()
    }

    @Test
    fun testNoteCountBarDisplayed() {
        composeTestRule
            .onNodeWithText("Synced just now")
            .assertIsDisplayed()
    }

    @Test
    fun test3DotMenuOpens() {
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule
            .onNodeWithText("Sort by")
            .assertIsDisplayed()
    }

    @Test
    fun testSortMenuShowsOptions() {
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule
            .onNodeWithText("Sort by")
            .performClick()
        composeTestRule
            .onNodeWithText("Newest first")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Oldest first")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Title A-Z")
            .assertIsDisplayed()
    }

    @Test
    fun testAboutScreenNavigationFromMenu() {
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()
        composeTestRule
            .onNodeWithText("About")
            .performClick()
        composeTestRule
            .onNodeWithText("Smart Notes Manager")
            .assertIsDisplayed()
    }

    @Test
    fun testClearSearchButtonAppearsAfterTyping() {
        composeTestRule
            .onNodeWithText("Search notes...")
            .performTextInput("test")
        composeTestRule
            .onNodeWithContentDescription("Clear search")
            .assertIsDisplayed()
    }
}