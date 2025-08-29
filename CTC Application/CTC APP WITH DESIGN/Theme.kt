package com.example.ctcappactual.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


// Dark theme colors
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = OrangeAccent,
    tertiary = OrangeAccent,
    background = Color.Black,
    surface = Color.DarkGray,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White
)

// Light theme colors
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = OrangeAccent,
    tertiary = OrangeAccent,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun CtcappactualTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}