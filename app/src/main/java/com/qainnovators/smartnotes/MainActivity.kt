package com.qainnovators.smartnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.qainnovators.smartnotes.navigation.NavGraph
import com.qainnovators.smartnotes.ui.theme.SmartNotesManagerTheme
import com.qainnovators.smartnotes.viewmodel.NoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartNotesManagerTheme {
                val navController = rememberNavController()
                val viewModel: NoteViewModel = viewModel()
                NavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}