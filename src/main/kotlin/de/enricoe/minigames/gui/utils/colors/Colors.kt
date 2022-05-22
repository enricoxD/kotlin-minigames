package de.enricoe.minigames.gui.utils.colors

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import de.enricoe.minigames.gui.Application

@Composable
fun ContentColor() = LocalContentColor.current

@Composable
fun DefaultTextColor() =
    if (Application.isDarkMode) Color(0.9019608F, 0.88235295F, 0.8980392F)
    else Color(0.10980392F, 0.105882354F, 0.12156863F)

@Composable
fun colorScheme() = MaterialTheme.colorScheme

fun Color.add(red: Float, green: Float, blue: Float, alpha: Float): Color {
    val red = this.red + red
    val green = this.green + green
    val blue = this.blue + blue
    val alpha = this.alpha + alpha
    return Color(red, green, blue, alpha, ColorSpaces.Srgb)
}