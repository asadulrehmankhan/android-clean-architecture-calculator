package io.github.aloussase.calculator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aloussase.calculator.data.HistoryItem
import io.github.aloussase.calculator.repository.CalculatorRepository
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnComputeResult
import io.github.aloussase.calculator.ui.CalculatorEvent.OnInput
import io.github.aloussase.calculator.ui.CalculatorEvent.OnOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CalculatorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val items = repository.getAllHistoryItems()
            _state.update {
                it.copy(
                    history = items
                )
            }
        }
    }

    fun onEvent(evt: CalculatorEvent) {
        when (evt) {
            is OnInput -> {
                _state.update {
                    it.copy(
                        input = it.input + evt.input
                    )
                }
            }

            is OnOperation -> {
                _state.update {
                    it.copy(
                        input = it.input + evt.operation
                    )
                }
            }

            is OnComputeResult -> {
                viewModelScope.launch {
                    val result = repository.calculate(_state.value.input)
                    val item = HistoryItem(content = _state.value.input)
                    repository.createHistoryItem(item)
                    _state.update {
                        it.copy(
                            result = result,
                            history = it.history + item
                        )
                    }
                }
            }

            is OnClear -> {
                _state.update {
                    it.copy(
                        input = "",
                        result = 0
                    )
                }
            }
        }
    }
}