package com.qainnovators.smartnotes.ui.screens

import androidx.compose.ui.graphics.Color

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Work" -> Color(0xFF1565C0)
        "Study" -> Color(0xFF6A1B9A)
        "Personal" -> Color(0xFF2E7D32)
        "Ideas" -> Color(0xFFE65100)
        else -> Color(0xFF37474F)
    }
}