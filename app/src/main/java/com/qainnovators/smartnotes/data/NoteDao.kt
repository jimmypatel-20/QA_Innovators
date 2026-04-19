package com.qainnovators.smartnotes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY isPinned DESC, timestamp DESC")
    fun getNotesByCategory(category: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY isPinned DESC, timestamp DESC")
    fun searchNotes(query: String): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, title ASC")
    fun getNotesSortedByTitleAsc(): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, timestamp ASC")
    fun getNotesSortedByOldest(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isFavourite = 1 ORDER BY timestamp DESC")
    fun getFavouriteNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isPinned = 1 ORDER BY timestamp DESC")
    fun getPinnedNotes(): Flow<List<Note>>
}