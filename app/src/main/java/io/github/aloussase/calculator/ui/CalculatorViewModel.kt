package io.github.aloussase.calculator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aloussase.calculator.data.HistoryItem
import io.github.aloussase.calculator.repository.ICalculatorRepository
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClearError
import io.github.aloussase.calculator.ui.CalculatorEvent.OnComputeResult
import io.github.aloussase.calculator.ui.CalculatorEvent.OnDeleteHistoryItem
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryItemClicked
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
    private val repository: ICalculatorRepository
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
                    val id = repository.createHistoryItem(item)
                    _state.update {
                        if (result == null) {
                            it.copy(
                                hadError = true
                            )
                        } else {
                            it.copy(
                                result = result,
                                history = it.history + item.copy(id = id)
                            )
                        }
                    }
                }
            }

            is OnHistoryItemClicked -> {
                _state.update {
                    it.copy(
                        input = evt.item,
                        result = 0f,
                    )
                }
            }

            is OnHistoryClear -> {
                viewModelScope.launch {
                    repository.deleteAllHistoryItems()
                    _state.update {
                        it.copy(
                            history = emptyList()
                        )
                    }
                }
            }

            is OnDeleteHistoryItem -> {
                viewModelScope.launch {
                    repository.deleteOneItem(evt.itemId)
                    _state.update {
                        it.copy(
                            history = it.history.filter { item ->
                                item.id != evt.itemId
                            }
                        )
                    }
                }
            }

            is OnClear -> {
                _state.update {
                    it.copy(
                        input = "",
                        result = 0f
                    )
                }
            }

            is OnClearError -> {
                _state.update {
                    it.copy(
                        hadError = false
                    )
                }
            }
        }
    }
}