package com.example.myapplication2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Raspberry,
    onPrimary = Color.White,
    secondary = TealDark,
    onSecondary = Color.White,
    tertiary = Pinkish,
    onTertiary = Color.White,
    background = LightGray,
    onBackground = Color(0xFF333333),
    surface = Color.White,
    onSurface = Color(0xFF333333),
)

private val DarkColorScheme = darkColorScheme(
    primary = Raspberry,
    onPrimary = Color.White,
    secondary = TealDark,
    onSecondary = Color.White,
    tertiary = Pinkish,
    onTertiary = Color.White,
    background = DarkGray,
    onBackground = Color(0xFFEAEAEA),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEAEAEA),
)

@Composable
fun MyApplication2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Opción: usar dynamic color en Android 12+
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
