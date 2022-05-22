package de.enricoe.minigames.game

import de.enricoe.minigames.game.player.AIPlayer
import de.enricoe.minigames.game.player.AllowAI

abstract class GameAI<G: AllowAI>(val game: G) {
    abstract fun run(aiPlayer: AIPlayer)
}