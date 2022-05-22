package de.enricoe.minigames.game.games.tictactoe

class TicTacToeSlot(val row: Int, val column: Int) {
    companion object {
        val AllSlots = mutableSetOf<TicTacToeSlot>()

        val RowOneSlotOne = TicTacToeSlot(0, 0)
        val RowOneSlotTwo = TicTacToeSlot(0, 1)
        val RowOneSlotThree = TicTacToeSlot(0, 2)
        
        val RowTwoSlotOne = TicTacToeSlot(1, 0)
        val RowTwoSlotTwo = TicTacToeSlot(1, 1)
        val RowTwoSlotThree = TicTacToeSlot(1, 2)

        val RowThreeSlotOne = TicTacToeSlot(2, 0)
        val RowThreeSlotTwo = TicTacToeSlot(2, 1)
        val RowThreeSlotThree = TicTacToeSlot(2, 2)
        fun getByInt(row: Int, column: Int) = AllSlots.first { slot -> slot.row == row && slot.column == column }
    }

    init {
        AllSlots += this
    }
}

class TicTacToeRow(vararg slots: TicTacToeSlot) {
    val slots = listOf(*slots)

    companion object {
        val AllRows = mutableSetOf<TicTacToeRow>()

        val RowOne = TicTacToeRow(TicTacToeSlot.RowOneSlotOne, TicTacToeSlot.RowOneSlotTwo, TicTacToeSlot.RowOneSlotThree)
        val RowTwo = TicTacToeRow(TicTacToeSlot.RowTwoSlotOne, TicTacToeSlot.RowTwoSlotTwo, TicTacToeSlot.RowTwoSlotThree)
        val RowThree = TicTacToeRow(TicTacToeSlot.RowThreeSlotOne, TicTacToeSlot.RowThreeSlotTwo, TicTacToeSlot.RowThreeSlotThree)

        val ColumnOne = TicTacToeRow(TicTacToeSlot.RowOneSlotOne, TicTacToeSlot.RowTwoSlotOne, TicTacToeSlot.RowThreeSlotOne)
        val ColumnTwo = TicTacToeRow(TicTacToeSlot.RowOneSlotTwo, TicTacToeSlot.RowTwoSlotTwo, TicTacToeSlot.RowThreeSlotTwo)
        val ColumnThree = TicTacToeRow(TicTacToeSlot.RowOneSlotThree, TicTacToeSlot.RowTwoSlotThree, TicTacToeSlot.RowThreeSlotThree)

        val DiagonalLeftToRight = TicTacToeRow(TicTacToeSlot.RowOneSlotOne, TicTacToeSlot.RowTwoSlotTwo, TicTacToeSlot.RowThreeSlotThree)
        val DiagonalRightToLeft = TicTacToeRow(TicTacToeSlot.RowOneSlotThree, TicTacToeSlot.RowTwoSlotTwo, TicTacToeSlot.RowThreeSlotOne)

        val HorizontalRows = mutableSetOf(RowOne, RowTwo, RowThree)
        val VerticalRows = mutableSetOf(ColumnOne, ColumnTwo, ColumnThree)
        fun getBySlot(slot: TicTacToeSlot) = AllRows.filter { row -> slot in row.slots }
    }

    init {
        AllRows += this
    }
}


