package com.qainnovators.smartnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qainnovators.smartnotes.data.Note
import com.qainnovators.smartnotes.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Int,
    viewModel: NoteViewModel,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var existingNote by remember { mutableStateOf<Note?>(null) }
    var titleError by remember { mutableStateOf(false) }

    val isEditMode = noteId != -1

    LaunchedEffect(noteId) {
        if (isEditMode) {
            val note = viewModel.getNoteById(noteId)
            note?.let {
                existingNote = it
                title = it.title
                description = it.description
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Note" else "New Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = {
                    if (it.length <= 100) {
                        title = it
                        titleError = false
                    }
                },
                label = { Text("Title *") },
                placeholder = { Text("Enter a short, clear title...") },
                isError = titleError,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (titleError) {
                            Text(
                                text = "Title cannot be empty",
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Text("${title.length}/100")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 500) description = it
                },
                label = { Text("Description") },
                placeholder = { Text("Write the full note content here...") },
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("${description.length}/500")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }
                    if (isEditMode && existingNote != null) {
                        viewModel.updateNote(
                            existingNote!!.copy(
                                title = title.trim(),
                                description = description.trim(),
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    } else {
                        viewModel.insertNote(
                            Note(
                                title = title.trim(),
                                description = description.trim()
                            )
                        )
                    }
                    onSaveClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Save Note",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // Discard button
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Discard Changes",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Text(
                text = "* Required field",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}