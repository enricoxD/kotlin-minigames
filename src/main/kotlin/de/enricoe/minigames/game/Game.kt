package de.enricoe.minigames.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import de.enricoe.minigames.game.games.tictactoe.TicTacToeProperties
import de.enricoe.minigames.game.player.Player
import de.enricoe.minigames.gui.screens.openGame
import kotlin.reflect.KClass

lateinit var allGames: MutableList<GameProperties>

abstract class Game {
    abstract val properties: GameProperties
    open var winner: Player? = null
    open var isRunning by mutableStateOf(true)

    @Composable open fun drawHeader() {}
    @Composable abstract fun drawGame()
    @Composable open fun drawFooter() {}

    fun start() {
        openGame = this
    }
}

class GameProperties(
    val name: String,
    val description: String,
    //val imageName: String,
    val maxPlayers: Int,
    val icon: ImageVector,
    val kclass: KClass<out Game>
)

fun registerGames() {
    allGames = mutableListOf(
        TicTacToeProperties,
    )
}