package com.qainnovators.smartnotes

import com.qainnovators.smartnotes.data.Note
import org.junit.Assert.*
import org.junit.Test

class NoteEntityTest {

    @Test
    fun testNoteCreationWithAllFields() {
        val note = Note(
            id = 1,
            title = "Test Title",
            description = "Test Description",
            timestamp = 1000L
        )
        assertEquals(1, note.id)
        assertEquals("Test Title", note.title)
        assertEquals("Test Description", note.description)
        assertEquals(1000L, note.timestamp)
    }

    @Test
    fun testNoteDefaultTimestamp() {
        val before = System.currentTimeMillis()
        val note = Note(title = "Hello", description = "World")
        val after = System.currentTimeMillis()
        assertTrue(note.timestamp in before..after)
    }

    @Test
    fun testNoteDefaultIdIsZero() {
        val note = Note(title = "Title", description = "Desc")
        assertEquals(0, note.id)
    }

    @Test
    fun testEmptyDescription() {
        val note = Note(title = "Title Only", description = "")
        assertEquals("", note.description)
    }

    @Test
    fun testNoteCopyUpdatesTitle() {
        val original = Note(id = 1, title = "Old Title", description = "Desc")
        val updated = original.copy(title = "New Title")
        assertEquals("New Title", updated.title)
        assertEquals(original.description, updated.description)
        assertEquals(original.id, updated.id)
    }
}