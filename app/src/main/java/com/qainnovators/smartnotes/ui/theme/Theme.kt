package com.qainnovators.smartnotes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = PurpleGrey40,
    onSecondary = White,
    secondaryContainer = PurpleGrey90,
    onSecondaryContainer = PurpleGrey10,
    tertiary = Pink40,
    onTertiary = White,
    tertiaryContainer = Pink90,
    onTertiaryContainer = Pink10,
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Background,
    onBackground = Black,
    surface = Surface,
    onSurface = Black,
    onSurfaceVariant = PurpleGrey40,
    surfaceVariant = Purple95,
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = PurpleGrey80,
    onSecondary = PurpleGrey20,
    secondaryContainer = PurpleGrey40,
    onSecondaryContainer = PurpleGrey90,
    tertiary = Pink80,
    onTertiary = Pink20,
    tertiaryContainer = Pink40,
    onTertiaryContainer = Pink90,
    error = Red80,
    onError = Red20,
    errorContainer = Red40,
    onErrorContainer = Red90,
    background = DarkBackground,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    onSurfaceVariant = PurpleGrey80,
    surfaceVariant = PurpleGrey20,
)

@Composable
fun SmartNotesManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}