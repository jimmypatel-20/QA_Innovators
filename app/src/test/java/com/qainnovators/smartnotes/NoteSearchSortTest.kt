package com.qainnovators.smartnotes

import com.qainnovators.smartnotes.data.Note
import org.junit.Assert.*
import org.junit.Test

class NoteSearchSortTest {

    private val testNotes = listOf(
        Note(id = 1, title = "Android Dev", description = "Kotlin stuff", category = "Work", timestamp = 3000L),
        Note(id = 2, title = "Grocery List", description = "Milk eggs bread", category = "Personal", timestamp = 1000L),
        Note(id = 3, title = "Study Plan", description = "Chapter 4 review", category = "Study", timestamp = 2000L),
        Note(id = 4, title = "Project Ideas", description = "New app concept", category = "Ideas", timestamp = 4000L)
    )

    @Test
    fun testSearchByTitleReturnsCorrectNote() {
        val result = testNotes.filter {
            it.title.contains("Grocery", ignoreCase = true) ||
            it.description.contains("Grocery", ignoreCase = true)
        }
        assertEquals(1, result.size)
        assertEquals("Grocery List", result[0].title)
    }

    @Test
    fun testSearchByDescriptionReturnsCorrectNote() {
        val result = testNotes.filter {
            it.description.contains("Chapter", ignoreCase = true)
        }
        assertEquals(1, result.size)
        assertEquals("Study Plan", result[0].title)
    }

    @Test
    fun testSearchEmptyQueryReturnsAll() {
        val query = ""
        val result = if (query.isBlank()) testNotes
        else testNotes.filter { it.title.contains(query, ignoreCase = true) }
        assertEquals(4, result.size)
    }

    @Test
    fun testFilterByCategoryWork() {
        val result = testNotes.filter { it.category == "Work" }
        assertEquals(1, result.size)
        assertEquals("Android Dev", result[0].title)
    }

    @Test
    fun testFilterByCategoryPersonal() {
        val result = testNotes.filter { it.category == "Personal" }
        assertEquals(1, result.size)
        assertEquals("Grocery List", result[0].title)
    }

    @Test
    fun testSortByNewest() {
        val sorted = testNotes.sortedByDescending { it.timestamp }
        assertEquals("Project Ideas", sorted[0].title)
        assertEquals("Grocery List", sorted[3].title)
    }

    @Test
    fun testSortByOldest() {
        val sorted = testNotes.sortedBy { it.timestamp }
        assertEquals("Grocery List", sorted[0].title)
        assertEquals("Project Ideas", sorted[3].title)
    }

    @Test
    fun testSortByTitleAZ() {
        val sorted = testNotes.sortedBy { it.title }
        assertEquals("Android Dev", sorted[0].title)
        assertEquals("Study Plan", sorted[3].title)
    }

    @Test
    fun testPinnedNotesAppearFirst() {
        val withPinned = testNotes.toMutableList()
        withPinned[1] = withPinned[1].copy(isPinned = true)
        val sorted = withPinned.sortedByDescending { it.isPinned }
        assertTrue(sorted[0].isPinned)
    }

    @Test
    fun testFavouriteFilterReturnsOnlyFavourites() {
        val withFav = testNotes.toMutableList()
        withFav[0] = withFav[0].copy(isFavourite = true)
        withFav[2] = withFav[2].copy(isFavourite = true)
        val result = withFav.filter { it.isFavourite }
        assertEquals(2, result.size)
    }
}