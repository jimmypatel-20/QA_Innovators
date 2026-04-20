package com.qainnovators.smartnotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qainnovators.smartnotes.data.Note
import com.qainnovators.smartnotes.viewmodel.NoteViewModel

val noteColors = listOf(
    "Default" to Color(0xFFFFFFFF),
    "Red" to Color(0xFFFFCDD2),
    "Orange" to Color(0xFFFFE0B2),
    "Yellow" to Color(0xFFFFF9C4),
    "Green" to Color(0xFFC8E6C9),
    "Blue" to Color(0xFFBBDEFB),
    "Purple" to Color(0xFFE1BEE7)
)

val templates = mapOf(
    "Meeting" to "Attendees:\n\nAgenda:\n\nAction Items:\n\nNext Meeting:",
    "Study" to "Topic:\n\nKey Points:\n\nSummary:\n\nQuestions:",
    "Todo" to "Today:\n- \n- \n- \n\nThis Week:\n- \n- \n\nSomeday:\n- ",
    "Journal" to "How I feel today:\n\nWhat happened:\n\nWhat I learned:\n\nGrateful for:"
)

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
    var selectedCategory by remember { mutableStateOf("Personal") }
    var selectedColor by remember { mutableStateOf("Default") }
    var isFavourite by remember { mutableStateOf(false) }
    var isPinned by remember { mutableStateOf(false) }
    var existingNote by remember { mutableStateOf<Note?>(null) }
    var titleError by remember { mutableStateOf(false) }
    var showTemplates by remember { mutableStateOf(false) }

    val isEditMode = noteId != -1
    val categories = listOf("Work", "Study", "Personal", "Ideas")

    // Title counter color
    val titleCountColor = when {
        title.length >= 95 -> MaterialTheme.colorScheme.error
        title.length >= 80 -> Color(0xFFE65100)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    // Description counter color
    val descCountColor = when {
        description.length >= 475 -> MaterialTheme.colorScheme.error
        description.length >= 400 -> Color(0xFFE65100)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    LaunchedEffect(noteId) {
        if (isEditMode) {
            val note = viewModel.getNoteById(noteId)
            note?.let {
                existingNote = it
                title = it.title
                description = it.description
                selectedCategory = it.category
                selectedColor = it.color
                isFavourite = it.isFavourite
                isPinned = it.isPinned
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
                actions = {
                    // Favourite toggle
                    IconButton(onClick = { isFavourite = !isFavourite }) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Default.Star
                            else Icons.Default.StarOutline,
                            contentDescription = "Favourite",
                            tint = if (isFavourite) Color(0xFFFFC107)
                            else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    // Pin toggle
                    IconButton(onClick = { isPinned = !isPinned }) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pin",
                            tint = if (isPinned) Color(0xFFFFC107)
                            else MaterialTheme.colorScheme.onPrimary
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Templates button
            OutlinedButton(
                onClick = { showTemplates = !showTemplates },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Templates")
            }

            // Template chips
            if (showTemplates) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    templates.keys.forEach { templateName ->
                        SuggestionChip(
                            onClick = {
                                description = templates[templateName] ?: ""
                                showTemplates = false
                            },
                            label = { Text(templateName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }

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
                        Text(
                            text = "${title.length}/100",
                            color = titleCountColor,
                            fontWeight = if (title.length >= 80)
                                FontWeight.Bold else FontWeight.Normal
                        )
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
                        Text(
                            text = "${description.length}/500",
                            color = descCountColor,
                            fontWeight = if (description.length >= 400)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )

            // Category selector
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                category,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }

            // Color picker
            Text(
                text = "Note color",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                noteColors.forEach { (colorName, color) ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (colorName == "Default")
                                    MaterialTheme.colorScheme.surfaceVariant
                                else color
                            )
                            .border(
                                width = if (selectedColor == colorName) 3.dp else 1.dp,
                                color = if (selectedColor == colorName)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = colorName }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Save button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }
                    val now = System.currentTimeMillis()
                    if (isEditMode && existingNote != null) {
                        viewModel.updateNote(
                            existingNote!!.copy(
                                title = title.trim(),
                                description = description.trim(),
                                category = selectedCategory,
                                color = selectedColor,
                                isFavourite = isFavourite,
                                isPinned = isPinned,
                                lastEdited = now
                            )
                        )
                    } else {
                        viewModel.insertNote(
                            Note(
                                title = title.trim(),
                                description = description.trim(),
                                category = selectedCategory,
                                color = selectedColor,
                                isFavourite = isFavourite,
                                isPinned = isPinned,
                                timestamp = now,
                                lastEdited = now
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
                    text = if (isEditMode) "Update Note" else "Save Note",
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