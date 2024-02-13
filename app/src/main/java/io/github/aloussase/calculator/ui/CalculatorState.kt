package io.github.aloussase.calculator.ui

import io.github.aloussase.calculator.data.HistoryItem

data class CalculatorState(
    val input: String = "",
    val result: Float = 0f,
    val history: List<HistoryItem> = emptyList(),
    val hadError: Boolean = false
)
