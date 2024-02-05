package io.github.aloussase.calculator.ui

import io.github.aloussase.calculator.data.HistoryItem

data class CalculatorState(
    val input: String = "",
    val result: Int = 0,
    val history: List<HistoryItem> = emptyList()
)
