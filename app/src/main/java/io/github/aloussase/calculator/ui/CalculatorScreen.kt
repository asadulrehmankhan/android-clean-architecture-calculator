package io.github.aloussase.calculator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnComputeResult
import io.github.aloussase.calculator.ui.CalculatorEvent.OnInput
import io.github.aloussase.calculator.ui.CalculatorEvent.OnOperation

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                text = state.input,
                modifier = Modifier.weight(3f)
            )
            TextField(
                text = "= " + state.result.toString(),
                modifier = Modifier.weight(1f)
            )
            Buttons(
                modifier = Modifier.weight(1f),
                onNumberPressed = { viewModel.onEvent(OnInput(it)) },
                onOperationPressed = { viewModel.onEvent(OnOperation(it)) },
                onEqualsPressed = { viewModel.onEvent(OnComputeResult) },
                onClearPressed = { viewModel.onEvent(OnClear) }
            )
        }
    }

}

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
    {
        Text(
            text = text,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    onNumberPressed: (String) -> Unit,
    onOperationPressed: (String) -> Unit,
    onEqualsPressed: () -> Unit,
    onClearPressed: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            RectangularButton(
                text = "1",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("1") }
            )
            RectangularButton(
                text = "2",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("2") }
            )
            RectangularButton(
                text = "3",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("3") }
            )
            RectangularButton(
                text = "C",
                modifier = Modifier.weight(1f),
                onClick = { onClearPressed() }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            RectangularButton(
                text = "4",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("4") }
            )
            RectangularButton(
                text = "5",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("5") }
            )
            RectangularButton(
                text = "6",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("6") }
            )
            RectangularButton(
                text = "+",
                modifier = Modifier.weight(1f),
                onClick = { onOperationPressed("+") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            RectangularButton(
                text = "7",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("7") }
            )
            RectangularButton(
                text = "8",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("8") }
            )
            RectangularButton(
                text = "9",
                modifier = Modifier.weight(1f),
                onClick = { onNumberPressed("9") }
            )
            RectangularButton(
                text = "=",
                modifier = Modifier.weight(1f),
                onClick = { onEqualsPressed() }
            )
        }
    }
}
