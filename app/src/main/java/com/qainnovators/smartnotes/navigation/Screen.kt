package com.qainnovators.smartnotes.navigation

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object About : Screen("about")
    object AddEditNote : Screen("add_edit_note?noteId={noteId}") {
        fun createRoute(noteId: Int = -1) = "add_edit_note?noteId=$noteId"
    }
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
}