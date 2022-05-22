package de.enricoe.minigames.gui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import de.enricoe.minigames.game.player.Player
import de.enricoe.minigames.game.registerGames
import de.enricoe.minigames.gui.screens.ActiveGameCard
import de.enricoe.minigames.gui.screens.HomeScreen
import de.enricoe.minigames.gui.utils.colors.add

object Application {
    val players = mutableStateListOf<Player>()
    var isDarkMode by mutableStateOf(false)
    var blurBackground by mutableStateOf(true)
}

@OptIn(ExperimentalAnimationApi::class)
fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Maximized)
    val blurBackground = Application.blurBackground

    registerGames()

    MaterialTheme(if (Application.isDarkMode) darkColorScheme() else lightColorScheme()) {
        Window(
            title = "Minigames",
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
                            onClick = { Application.isDarkMode = !Application.isDarkMode },
                        ) {
                            Icon(
                                if (Application.isDarkMode) FontAwesomeIcons.Solid.Lightbulb else FontAwesomeIcons.Solid.Moon,
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

            AnimatedVisibility(
                visible = blurBackground,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                val blur by animateFloatAsState(if (blurBackground) -0.15f else 0.0f, tween(250))
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(colorScheme.background.add(-0.09f, -0.09f, -0.09f, blur)),
                    contentAlignment = Alignment.Center
                ) {
                        UserLoginCard(state)
                        ActiveGameCard(state)
                }
            }
        }
    }
}

enum class Screen(val displayName: String, val icon: ImageVector) {
    HOME("Home", FontAwesomeIcons.Solid.Home),
    TICTACTOE("TicTacToe", FontAwesomeIcons.Solid.Table),
    PONG("Pong", FontAwesomeIcons.Solid.DotCircle),
}

private val navigationScreens = listOf(Screen.HOME)