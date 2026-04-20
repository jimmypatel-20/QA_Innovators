package com.qainnovators.smartnotes.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qainnovators.smartnotes.data.Note
import com.qainnovators.smartnotes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    viewModel: NoteViewModel,
    onEditClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var note by remember { mutableStateOf<Note?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(noteId) {
        note = viewModel.getNoteById(noteId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Note Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        // Share
                        DropdownMenuItem(
                            text = { Text("Share") },
                            leadingIcon = {
                                Icon(Icons.Default.Share, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                note?.let {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, it.title)
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "${it.title}\n\n${it.description}"
                                        )
                                    }
                                    context.startActivity(
                                        Intent.createChooser(shareIntent, "Share Note")
                                    )
                                }
                            }
                        )
                        // Copy
                        DropdownMenuItem(
                            text = { Text("Copy text") },
                            leadingIcon = {
                                Icon(Icons.Default.ContentCopy, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                note?.let {
                                    val clipboard = context.getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                    ) as ClipboardManager
                                    val clip = ClipData.newPlainText(
                                        "Note", "${it.title}\n\n${it.description}"
                                    )
                                    clipboard.setPrimaryClip(clip)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Note copied to clipboard"
                                        )
                                    }
                                }
                            }
                        )
                        // Pin
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (note?.isPinned == true) "Unpin"
                                    else "Pin to top"
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.PushPin, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                note?.let { viewModel.togglePin(it) }
                            }
                        )
                        // Favourite
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (note?.isFavourite == true)
                                        "Remove favourite"
                                    else "Add to favourites"
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    if (note?.isFavourite == true) Icons.Default.Star
                                    else Icons.Default.StarOutline,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                showMenu = false
                                note?.let { viewModel.toggleFavourite(it) }
                            }
                        )
                        // Duplicate
                        DropdownMenuItem(
                            text = { Text("Duplicate note") },
                            leadingIcon = {
                                Icon(Icons.Default.ContentCopy, contentDescription = null)
                            },
                            onClick = {
                                showMenu = false
                                note?.let {
                                    viewModel.duplicateNote(it)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Note duplicated")
                                    }
                                }
                            }
                        )
                        HorizontalDivider()
                        // Delete
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Delete",
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        note?.let { currentNote ->
            val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy  HH:mm", Locale.getDefault())
            val wordCount = if (currentNote.description.isBlank()) 0
            else currentNote.description.trim().split("\\s+".toRegex()).size
            val charCount = currentNote.description.length
            val readTime = maxOf(1, wordCount / 200)
            val categoryColor = getCategoryColor(currentNote.category)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = currentNote.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = categoryColor
                            ) {
                                Text(
                                    text = currentNote.category,
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp, vertical = 2.dp
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "ID: ${String.format("%05d", currentNote.id)}",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp, vertical = 2.dp
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            if (currentNote.isPinned) {
                                Icon(
                                    Icons.Default.PushPin,
                                    contentDescription = "Pinned",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            if (currentNote.isFavourite) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Favourite",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                // Timestamps
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Created",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = dateFormat.format(Date(currentNote.timestamp)),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Last edited",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = dateFormat.format(Date(currentNote.lastEdited)),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$wordCount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "words",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$charCount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "chars",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$readTime min",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "read time",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Note content
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "NOTE CONTENT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = androidx.compose.ui.unit.TextUnit(
                                1.5f,
                                androidx.compose.ui.unit.TextUnitType.Sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (currentNote.description.isNotBlank())
                                currentNote.description
                            else "No description provided.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentNote.description.isNotBlank())
                                MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Edit button
                Button(
                    onClick = { onEditClick(currentNote.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Note")
                }

                // Delete button
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Note")
                }

                // Share button
                OutlinedButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, currentNote.title)
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${currentNote.title}\n\n${currentNote.description}"
                            )
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share Note")
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Note")
                }

                // Export as TXT button
                OutlinedButton(
                    onClick = { exportNoteAsTxt(context, currentNote) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export as TXT")
                }
            }

            // Delete dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete Note") },
                    text = {
                        Text(
                            "Move \"${currentNote.title}\" to trash? You can restore it from the Trash screen."
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteNote(currentNote)
                                showDeleteDialog = false
                                onDeleteClick()
                            }
                        ) {
                            Text("Move to Trash", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

fun exportNoteAsTxt(context: Context, note: Note) {
    try {
        val fileName = "${note.title.replace(" ", "_")}_${note.id}.txt"
        val content = buildString {
            appendLine("Title: ${note.title}")
            appendLine("Category: ${note.category}")
            appendLine(
                "Created: ${
                    java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(note.timestamp))
                }"
            )
            appendLine(
                "Last Edited: ${
                    java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(note.lastEdited))
                }"
            )
            appendLine("---")
            appendLine(note.description)
        }
        val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(
            android.os.Environment.DIRECTORY_DOWNLOADS
        )
        val file = java.io.File(downloadsDir, fileName)
        file.writeText(content)
        android.widget.Toast.makeText(
            context,
            "Saved to Downloads/$fileName",
            android.widget.Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Export failed: ${e.message}",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}