package com.qainnovators.smartnotes.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qainnovators.smartnotes.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: NoteViewModel,
    onBackClick: () -> Unit
) {
    val notes by viewModel.allNotes.collectAsState()

    val totalNotes = notes.size
    val pinnedCount = notes.count { it.isPinned }
    val favouriteCount = notes.count { it.isFavourite }
    val lockedCount = notes.count { it.isLocked }

    val categoryCounts = mapOf(
        "Work" to notes.count { it.category == "Work" },
        "Study" to notes.count { it.category == "Study" },
        "Personal" to notes.count { it.category == "Personal" },
        "Ideas" to notes.count { it.category == "Ideas" }
    )

    val mostUsedCategory = categoryCounts.maxByOrNull { it.value }?.key ?: "None"
    val totalWords = notes.sumOf { note ->
        if (note.description.isBlank()) 0
        else note.description.trim().split("\\s+".toRegex()).size
    }

    val recentlyViewed = notes.sortedByDescending { it.lastEdited }.take(3)
    val duplicateGroups = notes.groupBy { it.title.trim().lowercase() }
        .filter { it.value.size > 1 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes Statistics") },
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

            // Overview stats grid
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "$totalNotes",
                    label = "Total Notes",
                    icon = Icons.Default.Notes,
                    color = MaterialTheme.colorScheme.primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "$totalWords",
                    label = "Total Words",
                    icon = Icons.Default.TextFields,
                    color = Color(0xFF1565C0)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "$pinnedCount",
                    label = "Pinned",
                    icon = Icons.Default.PushPin,
                    color = Color(0xFF6A1B9A)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "$favouriteCount",
                    label = "Favourites",
                    icon = Icons.Default.Star,
                    color = Color(0xFFE65100)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "$lockedCount",
                    label = "Locked",
                    icon = Icons.Default.Lock,
                    color = Color(0xFFB3261E)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = mostUsedCategory,
                    label = "Top Category",
                    icon = Icons.Default.Category,
                    color = Color(0xFF2E7D32)
                )
            }

            HorizontalDivider()

            // Notes by category
            Text(
                text = "Notes by Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categoryCounts.forEach { (category, count) ->
                        val color = getCategoryColor(category)
                        val progress = if (totalNotes > 0)
                            count.toFloat() / totalNotes.toFloat()
                        else 0f

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(10.dp),
                                        shape = MaterialTheme.shapes.small,
                                        color = color
                                    ) {}
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "$count notes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = color,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Recently viewed
            Text(
                text = "Recently Edited",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (recentlyViewed.isEmpty()) {
                Text(
                    text = "No notes yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recentlyViewed.forEachIndexed { index, note ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(28.dp),
                                        shape = MaterialTheme.shapes.small,
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = note.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = getCategoryColor(note.category)
                                ) {
                                    Text(
                                        text = note.category,
                                        modifier = Modifier.padding(
                                            horizontal = 6.dp, vertical = 2.dp
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                            }
                            if (index < recentlyViewed.size - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Duplicate notes warning
            if (duplicateGroups.isNotEmpty()) {
                Text(
                    text = "Possible Duplicates",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        duplicateGroups.forEach { (title, dupes) ->
                            Text(
                                text = "\"${dupes.first().title}\" — ${dupes.size} copies",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}