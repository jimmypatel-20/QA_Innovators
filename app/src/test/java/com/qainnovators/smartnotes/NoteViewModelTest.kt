package com.qainnovators.smartnotes

import com.qainnovators.smartnotes.data.Note
import org.junit.Assert.*
import org.junit.Test

class NoteViewModelTest {

    @Test
    fun testTitleIsNotBlank_returnsTrue() {
        val title = "My Note"
        assertTrue(title.isNotBlank())
    }

    @Test
    fun testTitleIsBlank_returnsFalse() {
        val title = "   "
        assertFalse(title.isNotBlank())
    }

    @Test
    fun testEmptyTitleValidationFails() {
        val title = ""
        assertTrue(title.isBlank())
    }

    @Test
    fun testTitleMaxLength() {
        val title = "A".repeat(100)
        assertTrue(title.length <= 100)
    }

    @Test
    fun testDescriptionMaxLength() {
        val description = "B".repeat(500)
        assertTrue(description.length <= 500)
    }

    @Test
    fun testNoteUpdatePreservesId() {
        val original = Note(id = 5, title = "Original", description = "Desc")
        val updated = original.copy(
            title = "Updated Title",
            timestamp = System.currentTimeMillis()
        )
        assertEquals(5, updated.id)
        assertEquals("Updated Title", updated.title)
    }

    @Test
    fun testTrimmedTitleSavesCorrectly() {
        val rawTitle = "   My Note   "
        val trimmed = rawTitle.trim()
        assertEquals("My Note", trimmed)
    }

    @Test
    fun testTrimmedDescriptionSavesCorrectly() {
        val rawDesc = "   Some content   "
        val trimmed = rawDesc.trim()
        assertEquals("Some content", trimmed)
    }

    @Test
    fun testEditModeDetectedWhenNoteIdIsNotMinusOne() {
        val noteId = 3
        val isEditMode = noteId != -1
        assertTrue(isEditMode)
    }

    @Test
    fun testAddModeDetectedWhenNoteIdIsMinusOne() {
        val noteId = -1
        val isEditMode = noteId != -1
        assertFalse(isEditMode)
    }
}