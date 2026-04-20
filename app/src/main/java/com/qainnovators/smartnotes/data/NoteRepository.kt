package com.qainnovators.smartnotes.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    val trashedNotes: Flow<List<Note>> = noteDao.getTrashedNotes()

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDao.updateNote(note.copy(isTrashed = true))
    suspend fun restoreNote(note: Note) = noteDao.updateNote(note.copy(isTrashed = false))
    suspend fun permanentlyDeleteNote(note: Note) = noteDao.deleteNote(note)
    suspend fun clearTrash() = noteDao.clearTrash()
    suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)

    fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query)
    fun getNotesByCategory(category: String): Flow<List<Note>> =
        noteDao.getNotesByCategory(category)
    fun getNotesSortedByTitleAsc(): Flow<List<Note>> = noteDao.getNotesSortedByTitleAsc()
    fun getNotesSortedByOldest(): Flow<List<Note>> = noteDao.getNotesSortedByOldest()
    fun getFavouriteNotes(): Flow<List<Note>> = noteDao.getFavouriteNotes()
    fun getPinnedNotes(): Flow<List<Note>> = noteDao.getPinnedNotes()
}