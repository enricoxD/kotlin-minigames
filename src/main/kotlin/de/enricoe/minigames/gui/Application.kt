package de.enricoe.minigames.gui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*
import de.enricoe.minigames.game.registerGames
import de.enricoe.minigames.gui.screens.ActiveGameCard
import de.enricoe.minigames.gui.screens.HomeScreen

object Application {
    var isDarkMode = mutableStateOf(false)
    var blurBackground = mutableStateOf(false)
}

fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    var isDarkMode by remember { Application.isDarkMode }
    val blurBackground by remember { Application.blurBackground }

    registerGames()

    MaterialTheme(if (isDarkMode) darkColorScheme() else lightColorScheme()) {
        Window(
            title = "Benutzerverwaltung",
            onCloseRequest = { exitApplication() },
            state = state
        ) {
            Surface(Modifier.fillMaxSize().blur(if (blurBackground) 5.dp else 0.dp)) {
                Row {
                    var currentScreen by remember { mutableStateOf(Screen.HOME) }

                    NavigationRail(Modifier.width(64.dp)) {
                        remember { navigationScreens }.forEach {
                            NavigationRailItem(
                                currentScreen == it,
                                onClick = { currentScreen = it },
                                icon = { Icon(it.icon, it.displayName, Modifier.size(32.dp)) },
                                label = { Text(it.displayName) }
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = { isDarkMode = !isDarkMode },
                        ) {
                            Icon(
                                if (isDarkMode) FontAwesomeIcons.Solid.Sun else FontAwesomeIcons.Solid.Moon,
                                "toggle theme",
                                Modifier.size(32.dp)
                            )
                        }
                    }

                    Surface(Modifier.fillMaxSize()) {
                        Box(Modifier.fillMaxSize()) {
                            when (currentScreen) {
                                Screen.HOME -> HomeScreen()
                                Screen.TICTACTOE -> TODO()
                                Screen.PONG -> TODO()
                            }
                        }
                    }
                }
            }

            ActiveGameCard(state)
        }
    }
}

enum class Screen(val displayName: String, val icon: ImageVector) {
    HOME("Home", FontAwesomeIcons.Solid.Home),
    TICTACTOE("TicTacToe", FontAwesomeIcons.Solid.Table),
    PONG("Pong", FontAwesomeIcons.Solid.DotCircle),
}

private val navigationScreens = listOf(Screen.HOME)