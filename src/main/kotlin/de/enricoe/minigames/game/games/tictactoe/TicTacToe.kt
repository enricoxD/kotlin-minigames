package de.enricoe.minigames.game.games.tictactoe

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.CircleNotch
import compose.icons.fontawesomeicons.solid.Table
import de.enricoe.minigames.game.Game
import de.enricoe.minigames.game.GameProperties
import de.enricoe.minigames.game.player.*
import de.enricoe.minigames.gui.utils.colors.colorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TicTacToeProperties = GameProperties(
    "TicTacToe",
    "TicTacToe is a game for two players who take turns marking the spaces in a three-by-three grid with X or O. The player who succeeds in placing three of their marks in a line is the winner",
    2,
    FontAwesomeIcons.Solid.Table
)

object TicTacToe: Game(TicTacToeProperties), TwoPlayers, AllowAI {
    override val playerOne: HumanPlayer = HumanPlayer("Player One")
    override val playerTwo: Player = AIPlayer()
    var currentPlayer: MutableState<Player> = mutableStateOf(playerOne)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val slots = mutableStateMapOf<TicTacToeSlot, Player>()
    val availableSlots get() = TicTacToeSlot.AllSlots.filter { slot -> slot !in slots }
    val winningSlots = mutableStateListOf<TicTacToeSlot>()

    fun mark(slot: TicTacToeSlot, forced: Boolean = false) {
        var player = currentPlayer.value
        if (player is AIPlayer && !forced) return
        if (slot in slots) return

        slots[slot] = player
        if (slots.size >= 5) {
            checkForWinner(slot)?.forEach { (row, player) ->
                winningSlots.addAll(row.slots)
                winner = player
            }
        }

        if (checkForEnd()) {
            isRunning.value = false
            return
        }
        player = if (player == playerOne) playerTwo else playerOne
        currentPlayer.value = player

        if (player is AIPlayer) {
            ai.currentBoard = slots.toMutableMap()
            coroutineScope.launch {
                delay((300..600).random().toLong())
                ai.run(player)
            }
        }
    }

    override val ai = TicTacToeAI

    private fun checkForWinner(slot: TicTacToeSlot): List<Pair<TicTacToeRow, Player>>? {
        val rows = TicTacToeRow.getBySlot(slot).mapNotNull { row ->
            val winner = checkRow(row)
            if (winner == null) null
            else row to winner
        }
        return rows.ifEmpty { null }
    }

    private fun checkRow(row: TicTacToeRow): Player? {
        if (row.slots.any { slot -> slot !in slots }) return null
        return row.slots.map { slot -> slots[slot] }.toSet().singleOrNull()
    }

    private fun checkForEnd(): Boolean = TicTacToeSlot.AllSlots.none { slot -> slot !in slots } || winner != null

    override fun reset() {
        slots.clear()
        winningSlots.clear()
        currentPlayer.value = playerOne
        isRunning.value = true
        winner = null
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun drawHeader() {
        val size by animateIntAsState(
            targetValue = if (isRunning.value) 16 else 27,
            animationSpec = tween(durationMillis = 200)
        )
        val padding by animateIntAsState(
            targetValue = if (isRunning.value) 24 else 196,
            animationSpec = tween(durationMillis = 300)
        )
        ElevatedCard(Modifier.padding(top = padding.dp)) {
            Box(Modifier.padding(vertical = 24.dp, horizontal = 32.dp), contentAlignment = Alignment.Center) {
                if (isRunning.value)
                    Text("${currentPlayer.value.name}'s turn", fontSize = size.sp)
                else {
                    Text("${winner?.name ?: "Nobody"} won", fontSize = size.sp)
                }
            }
        }
    }

    @Composable
    override fun drawGame() {
        Column(Modifier.clip(RoundedCornerShape(16.dp))) {
            TicTacToeRow.HorizontalRows.forEach { row ->
                Row {
                    row.slots.forEach { slot ->
                        val color by animateColorAsState(
                            targetValue = if (slot in winningSlots) colorScheme().inversePrimary else colorScheme().surfaceVariant,
                            animationSpec = tween(durationMillis = 450)
                        )
                        Surface(Modifier.size(128.dp).clickable { if (isRunning.value) mark(slot) }, color = color) {
                            val player = slots[slot]
                            val size by animateIntAsState(
                                targetValue = if (player == null) 0 else 96,
                                animationSpec = tween(durationMillis = 300)
                            )
                            val rotation by animateFloatAsState(
                                targetValue = if (slot in winningSlots) 0f else 360f,
                                animationSpec = tween(durationMillis = 400)
                            )
                            Box(contentAlignment = Alignment.Center) {
                                val icon = if (slots[slot] == playerOne) Icons.Default.Close
                                else if (slots[slot] == playerTwo) FontAwesomeIcons.Solid.CircleNotch
                                else Icons.Default.Warning
                                Icon(icon, "Player", Modifier.size(size.dp).rotate(rotation))
                            }
                        }
                    }
                }
            }
        }
    }
}

