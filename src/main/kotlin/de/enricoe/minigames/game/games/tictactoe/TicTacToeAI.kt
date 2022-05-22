package de.enricoe.minigames.game.games.tictactoe

import de.enricoe.minigames.game.GameAI
import de.enricoe.minigames.game.player.AIPlayer
import de.enricoe.minigames.game.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// This AI doesn't play the optional way so the player has a chance to win
class TicTacToeAI(ticTacToe: TicTacToe) : GameAI<TicTacToe>(ticTacToe) {
    private val humanPlayer = game.playerOne
    private val aiPlayer = game.playerTwo
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    lateinit var currentBoard: MutableMap<TicTacToeSlot, Player>
    private val availableSlots get() = TicTacToeSlot.AllSlots.filter { slot -> slot !in currentBoard }

    override fun run(aiPlayer: AIPlayer) {
        coroutineScope.launch {
            currentBoard = game.slots.toMutableMap()
            val slot = getBestMove()
            game.mark(slot, true)
        }
    }

    private fun getBestMove(): TicTacToeSlot {
        availableSlots.forEach { slot ->
            currentBoard[slot] = aiPlayer
            if (checkForWinner(slot) != null) {
                return slot
            }
            currentBoard.remove(slot)
        }

        availableSlots.forEach { slot ->
            currentBoard[slot] = humanPlayer
            if (checkForWinner(slot) != null) {
                return slot
            }
            currentBoard.remove(slot)
        }
        return availableSlots.random()
    }


    private fun checkForWinner(slot: TicTacToeSlot): List<Pair<TicTacToeRow, Player>>? {
        val rows = TicTacToeRow.getBySlot(slot).mapNotNull { row ->
            val winner = checkRow(row)
            if (winner == null) null
            else row to winner
        }
        return rows.ifEmpty { null }
    }

    private fun checkRow(row: TicTacToeRow): Player? {
        if (row.slots.any { slot -> slot !in currentBoard }) return null
        return row.slots.map { slot -> currentBoard[slot] }.toSet().singleOrNull()
    }
}