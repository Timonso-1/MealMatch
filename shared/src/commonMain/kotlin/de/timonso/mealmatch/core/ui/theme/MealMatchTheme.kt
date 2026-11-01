package de.timonso.mealmatch.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object MealMatchColors {
    val Green = Color(0xFF0B8A43)
    val GreenDark = Color(0xFF0A4B34)
    val GreenSoft = Color(0xFFE3EEDA)
    val GreenTint = Color(0xFFE8F4EA)
    val Orange = Color(0xFFF28C28)
    val Ink = Color(0xFF0F1B2D)
    val Muted = Color(0xFF5B6475)
    val Outline = Color(0xFFC9CED6)
    val Background = Color(0xFFF7F9F4)
}

private val LightColors = lightColorScheme(
    primary = MealMatchColors.Green,
    onPrimary = Color.White,
    secondary = MealMatchColors.Orange,
    background = MealMatchColors.Background,
    surface = Color.White,
    onSurface = MealMatchColors.Ink,
    onSurfaceVariant = MealMatchColors.Muted,
    outline = MealMatchColors.Outline,
    error = Color(0xFFB3261E),
)

@Composable
fun MealMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
