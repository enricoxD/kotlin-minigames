package de.enricoe.minigames.game.player

import de.enricoe.minigames.game.GameAI

open class Player(val name: String)

interface OnePlayer {
    val playerOne: HumanPlayer
}
interface TwoPlayers: OnePlayer {
    val playerTwo: Player
}
interface ThreePlayers: TwoPlayers {
    val playerThree: Player
}
interface FourPlayers: ThreePlayers {
    val playerFour: Player
}
interface AllowAI {
    val ai: GameAI<out AllowAI>
}