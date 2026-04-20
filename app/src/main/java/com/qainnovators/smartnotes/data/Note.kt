package com.qainnovators.smartnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String = "Personal",
    val color: String = "Default",
    val isPinned: Boolean = false,
    val isFavourite: Boolean = false,
    val isLocked: Boolean = false,
    val isTrashed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val lastEdited: Long = System.currentTimeMillis()
)