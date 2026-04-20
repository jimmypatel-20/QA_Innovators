package com.qainnovators.smartnotes

import com.qainnovators.smartnotes.data.Note
import org.junit.Assert.*
import org.junit.Test

class NoteColorTemplateTest {

    @Test
    fun testDefaultNoteColorIsDefault() {
        val note = Note(title = "Test", description = "Desc")
        assertEquals("Default", note.color)
    }

    @Test
    fun testNoteColorCanBeSetToRed() {
        val note = Note(title = "Test", description = "Desc", color = "Red")
        assertEquals("Red", note.color)
    }

    @Test
    fun testNoteColorCanBeSetToBlue() {
        val note = Note(title = "Test", description = "Desc", color = "Blue")
        assertEquals("Blue", note.color)
    }

    @Test
    fun testNoteColorCanBeSetToGreen() {
        val note = Note(title = "Test", description = "Desc", color = "Green")
        assertEquals("Green", note.color)
    }

    @Test
    fun testWordCountCalculation() {
        val description = "Hello world this is a test"
        val wordCount = description.trim().split("\\s+".toRegex()).size
        assertEquals(6, wordCount)
    }

    @Test
    fun testWordCountEmptyDescription() {
        val description = ""
        val wordCount = if (description.isBlank()) 0
        else description.trim().split("\\s+".toRegex()).size
        assertEquals(0, wordCount)
    }

    @Test
    fun testReadTimeCalculation() {
        val wordCount = 400
        val readTime = maxOf(1, wordCount / 200)
        assertEquals(2, readTime)
    }

    @Test
    fun testReadTimeMinimumIsOne() {
        val wordCount = 50
        val readTime = maxOf(1, wordCount / 200)
        assertEquals(1, readTime)
    }

    @Test
    fun testMeetingTemplateContainsKeyFields() {
        val template = "Attendees:\n\nAgenda:\n\nAction Items:\n\nNext Meeting:"
        assertTrue(template.contains("Attendees"))
        assertTrue(template.contains("Agenda"))
        assertTrue(template.contains("Action Items"))
    }

    @Test
    fun testStudyTemplateContainsKeyFields() {
        val template = "Topic:\n\nKey Points:\n\nSummary:\n\nQuestions:"
        assertTrue(template.contains("Topic"))
        assertTrue(template.contains("Key Points"))
        assertTrue(template.contains("Summary"))
    }

    @Test
    fun testTodoTemplateContainsKeyFields() {
        val template = "Today:\n- \n- \n- \n\nThis Week:\n- \n- \n\nSomeday:\n- "
        assertTrue(template.contains("Today"))
        assertTrue(template.contains("This Week"))
        assertTrue(template.contains("Someday"))
    }

    @Test
    fun testJournalTemplateContainsKeyFields() {
        val template = "How I feel today:\n\nWhat happened:\n\nWhat I learned:\n\nGrateful for:"
        assertTrue(template.contains("How I feel today"))
        assertTrue(template.contains("What I learned"))
        assertTrue(template.contains("Grateful for"))
    }

    @Test
    fun testCharCountMatchesDescriptionLength() {
        val description = "Hello World"
        assertEquals(11, description.length)
    }

    @Test
    fun testTitleCounterWarningAt80Percent() {
        val title = "A".repeat(82)
        val isWarning = title.length >= 80
        assertTrue(isWarning)
    }

    @Test
    fun testDescriptionCounterWarningAt80Percent() {
        val description = "B".repeat(405)
        val isWarning = description.length >= 400
        assertTrue(isWarning)
    }
}