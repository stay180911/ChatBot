package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = JusticeGold,
    onPrimary = NavyDark,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldOnContainer,
    secondary = CyanAccent,
    onSecondary = NavyDark,
    tertiary = CyanGlow,
    background = NavyDark,
    onBackground = OffWhite,
    surface = NavySurface,
    onSurface = OffWhite,
    surfaceVariant = NavyCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334E68)
)

private val LightColorScheme = lightColorScheme(
    primary = NavyDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2E8F0),
    onPrimaryContainer = NavyDark,
    secondary = JusticeGoldDark,
    onSecondary = Color.White,
    tertiary = CyanAccent,
    background = OffWhite,
    onBackground = NavyDark,
    surface = LightSurface,
    onSurface = SlateText,
    surfaceVariant = LightCard,
    onSurfaceVariant = SlateGray,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent legal tech branding
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

