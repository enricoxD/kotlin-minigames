package de.enricoe.minigames.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import de.enricoe.minigames.game.games.tictactoe.TicTacToe
import de.enricoe.minigames.game.player.AIPlayer
import de.enricoe.minigames.game.player.HumanPlayer
import de.enricoe.minigames.game.player.Player

lateinit var allGames: List<Game>

abstract class Game(val properties: GameProperties) {
    open var winner: Player? = null
    open var isRunning: MutableState<Boolean> = mutableStateOf(true)

    @Composable open fun drawHeader() {}
    @Composable abstract fun drawGame()
    @Composable open fun drawFooter() {}

    abstract fun reset()
}

class GameProperties(
    val name: String,
    val description: String,
    //val imageName: String,
    val maxPlayers: Int,
    val icon: ImageVector,
)

fun registerGames() {
    allGames = listOf(
        TicTacToe,
    )
}