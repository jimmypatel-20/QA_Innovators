package com.qainnovators.smartnotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qainnovators.smartnotes.ui.screens.AddEditNoteScreen
import com.qainnovators.smartnotes.ui.screens.NoteDetailScreen
import com.qainnovators.smartnotes.ui.screens.NoteListScreen
import com.qainnovators.smartnotes.viewmodel.NoteViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: NoteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(route = Screen.NoteList.route) {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteDetail.createRoute(noteId))
                },
                onAddClick = {
                    navController.navigate(Screen.AddEditNote.createRoute())
                }
            )
        }

        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
            NoteDetailScreen(
                noteId = noteId,
                viewModel = viewModel,
                onEditClick = { id ->
                    navController.navigate(Screen.AddEditNote.createRoute(id))
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.AddEditNote.route,
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
            AddEditNoteScreen(
                noteId = noteId,
                viewModel = viewModel,
                onSaveClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}