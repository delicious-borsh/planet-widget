package com.example.usefulwidgetapp

class CellsCalculator {

    fun getWidgetHeightInCells(minHeight: Int, maxHeight: Int): Int? {
        val range = ((minHeight + 1) until maxHeight)

        return when {
            range.contains(ARBITRARY_CHOSEN_CONSTANT_FOR_1_CELL) -> 1
            range.contains(ARBITRARY_CHOSEN_CONSTANT_FOR_2_CELLS) -> 2
            range.contains(ARBITRARY_CHOSEN_CONSTANT_FOR_3_CELLS) -> 3
            range.contains(ARBITRARY_CHOSEN_CONSTANT_FOR_4_CELLS) -> 4
            else -> null
        }
    }
}

private const val ARBITRARY_CHOSEN_CONSTANT_FOR_1_CELL = 50
private const val ARBITRARY_CHOSEN_CONSTANT_FOR_2_CELLS = 150
private const val ARBITRARY_CHOSEN_CONSTANT_FOR_3_CELLS = 250
private const val ARBITRARY_CHOSEN_CONSTANT_FOR_4_CELLS = 350