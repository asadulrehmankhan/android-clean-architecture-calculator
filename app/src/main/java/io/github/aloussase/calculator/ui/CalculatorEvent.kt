package io.github.aloussase.calculator.ui

sealed class CalculatorEvent {
    data class OnInput(val input: String) : CalculatorEvent()
    data class OnOperation(val operation: String) : CalculatorEvent()
    data object OnComputeResult : CalculatorEvent()
    data object OnClear : CalculatorEvent()
    data class OnHistoryItemClicked(val item: String) : CalculatorEvent()

    data object OnHistoryClear : CalculatorEvent()
}