package com.qainnovators.smartnotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.qainnovators.smartnotes.data.AppDatabase
import com.qainnovators.smartnotes.data.Note
import com.qainnovators.smartnotes.data.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SortOrder { NEWEST, OLDEST, A_Z }

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val isDarkMode = MutableStateFlow(false)

    val allNotes: StateFlow<List<Note>>
    val trashedNotes: StateFlow<List<Note>>

    init {
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)

        allNotes = combine(
            searchQuery,
            selectedCategory,
            sortOrder
        ) { query, category, sort ->
            Triple(query, category, sort)
        }.flatMapLatest { (query, category, sort) ->
            when {
                query.isNotBlank() -> repository.searchNotes(query)
                category != "All" -> repository.getNotesByCategory(category)
                sort == SortOrder.OLDEST -> repository.getNotesSortedByOldest()
                sort == SortOrder.A_Z -> repository.getNotesSortedByTitleAsc()
                else -> repository.allNotes
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        trashedNotes = repository.trashedNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun restoreNote(note: Note) = viewModelScope.launch {
        repository.restoreNote(note)
    }

    fun permanentlyDeleteNote(note: Note) = viewModelScope.launch {
        repository.permanentlyDeleteNote(note)
    }

    fun clearTrash() = viewModelScope.launch {
        repository.clearTrash()
    }

    fun duplicateNote(note: Note) = viewModelScope.launch {
        repository.insertNote(
            note.copy(
                id = 0,
                title = "${note.title} (copy)",
                timestamp = System.currentTimeMillis(),
                lastEdited = System.currentTimeMillis()
            )
        )
    }

    suspend fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }

    fun togglePin(note: Note) = viewModelScope.launch {
        repository.updateNote(note.copy(isPinned = !note.isPinned))
    }

    fun toggleFavourite(note: Note) = viewModelScope.launch {
        repository.updateNote(note.copy(isFavourite = !note.isFavourite))
    }

    fun toggleLock(note: Note) = viewModelScope.launch {
        repository.updateNote(note.copy(isLocked = !note.isLocked))
    }

    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setCategory(category: String) { selectedCategory.value = category }
    fun setSortOrder(order: SortOrder) { sortOrder.value = order }
    fun toggleDarkMode() { isDarkMode.value = !isDarkMode.value }
}