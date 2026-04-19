package com.qainnovators.smartnotes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qainnovators.smartnotes.data.AppDatabase
import com.qainnovators.smartnotes.data.Note
import com.qainnovators.smartnotes.data.NoteDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        noteDao = database.noteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertNoteAndRetrieve() = runTest {
        val note = Note(title = "Test Note", description = "Test Desc")
        noteDao.insertNote(note)
        val notes = noteDao.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Test Note", notes[0].title)
    }

    @Test
    fun testInsertMultipleNotes() = runTest {
        noteDao.insertNote(Note(title = "Note 1", description = "Desc 1"))
        noteDao.insertNote(Note(title = "Note 2", description = "Desc 2"))
        noteDao.insertNote(Note(title = "Note 3", description = "Desc 3"))
        val notes = noteDao.getAllNotes().first()
        assertEquals(3, notes.size)
    }

    @Test
    fun testDeleteNote() = runTest {
        val note = Note(title = "To Delete", description = "Desc")
        noteDao.insertNote(note)
        val inserted = noteDao.getAllNotes().first()[0]
        noteDao.deleteNote(inserted)
        val notes = noteDao.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun testUpdateNote() = runTest {
        val note = Note(title = "Original", description = "Old Desc")
        noteDao.insertNote(note)
        val inserted = noteDao.getAllNotes().first()[0]
        val updated = inserted.copy(title = "Updated", description = "New Desc")
        noteDao.updateNote(updated)
        val result = noteDao.getNoteById(inserted.id)
        assertEquals("Updated", result?.title)
        assertEquals("New Desc", result?.description)
    }

    @Test
    fun testGetNoteById() = runTest {
        val note = Note(title = "Find Me", description = "By ID")
        noteDao.insertNote(note)
        val inserted = noteDao.getAllNotes().first()[0]
        val found = noteDao.getNoteById(inserted.id)
        assertNotNull(found)
        assertEquals("Find Me", found?.title)
    }

    @Test
    fun testGetNoteByIdReturnsNullIfNotFound() = runTest {
        val result = noteDao.getNoteById(999)
        assertNull(result)
    }

    @Test
    fun testNotesOrderedByTimestampDescending() = runTest {
        noteDao.insertNote(Note(title = "First", description = "", timestamp = 1000L))
        noteDao.insertNote(Note(title = "Second", description = "", timestamp = 2000L))
        noteDao.insertNote(Note(title = "Third", description = "", timestamp = 3000L))
        val notes = noteDao.getAllNotes().first()
        assertEquals("Third", notes[0].title)
        assertEquals("First", notes[2].title)
    }

    @Test
    fun testInsertNoteWithEmptyDescription() = runTest {
        val note = Note(title = "No Desc", description = "")
        noteDao.insertNote(note)
        val notes = noteDao.getAllNotes().first()
        assertEquals("", notes[0].description)
    }

    @Test
    fun testDatabaseIsEmptyOnStart() = runTest {
        val notes = noteDao.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun testInsertReplaceOnConflict() = runTest {
        val note = Note(id = 1, title = "Original", description = "Desc")
        noteDao.insertNote(note)
        val replacement = Note(id = 1, title = "Replaced", description = "New")
        noteDao.insertNote(replacement)
        val notes = noteDao.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Replaced", notes[0].title)
    }
}