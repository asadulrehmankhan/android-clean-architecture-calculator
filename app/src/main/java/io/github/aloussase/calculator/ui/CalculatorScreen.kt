package io.github.aloussase.calculator.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.aloussase.calculator.R
import io.github.aloussase.calculator.repository.NullCalculatorRepository
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnClearError
import io.github.aloussase.calculator.ui.CalculatorEvent.OnComputeResult
import io.github.aloussase.calculator.ui.CalculatorEvent.OnInput
import io.github.aloussase.calculator.ui.CalculatorEvent.OnOperation
import io.github.aloussase.calculator.ui.CalculatorEvent.OnBackSpace
import io.github.aloussase.calculator.ui.theme.CalculatorTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CalculatorScreenPreview() {
    val viewModel = CalculatorViewModel(NullCalculatorRepository())
    val snackbar = remember { SnackbarHostState() }

    CalculatorTheme {
        CalculatorScreen(
            viewModel,
            snackbar
        )
    }
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    snackbar: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.hadError) {
        if (state.hadError) {
            snackbar.showSnackbar("There was an error calculating your expression")
            viewModel.onEvent(OnClearError)
        }
    }

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
                modifier = Modifier.weight(2f),
                onNumberPressed = { viewModel.onEvent(OnInput(it)) },
                onOperationPressed = { viewModel.onEvent(OnOperation(it)) },
                onEqualsPressed = { viewModel.onEvent(OnComputeResult) },
                onClearPressed = { viewModel.onEvent(OnClear) },
                onBackSpacePressed = { viewModel.onEvent(OnBackSpace) }
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
    onBackSpacePressed: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            RectangularButton(
                text = "C",
                modifier = Modifier.weight(1f),
                textColor = MaterialTheme.colorScheme.primary,
                onClick = { onClearPressed() }
            )
            OperationButton("(", onOperationPressed, Modifier.weight(1f))
            OperationButton(")", onOperationPressed, Modifier.weight(1f))
            OperationButton("/", onOperationPressed, Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberButton("7", onNumberPressed, Modifier.weight(1f))
            NumberButton("8", onNumberPressed, Modifier.weight(1f))
            NumberButton("9", onNumberPressed, Modifier.weight(1f))
            OperationButton("*", onOperationPressed, Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberButton("4", onNumberPressed, Modifier.weight(1f))
            NumberButton("5", onNumberPressed, Modifier.weight(1f))
            NumberButton("6", onNumberPressed, Modifier.weight(1f))
            OperationButton("-", onOperationPressed, Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NumberButton("1", onNumberPressed, Modifier.weight(1f))
            NumberButton("2", onNumberPressed, Modifier.weight(1f))
            NumberButton("3", onNumberPressed, Modifier.weight(1f))
            OperationButton("+", onOperationPressed, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberButton("0", onNumberPressed, Modifier.weight(2f))
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Backspace",
                modifier = Modifier.clickable { onBackSpacePressed() }
                    .weight(1f)
                    .padding(18.dp, 5.dp, 18.dp, 10.dp)
            )
            OperationButton(".", onOperationPressed, Modifier.weight(1f))
            RectangularButton(
                text = "=",
                modifier = Modifier.weight(1f),
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { onEqualsPressed() }
            )
        }
    }
}


@Composable
private fun NumberButton(
    number: String,
    onNumberPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    RectangularButton(
        text = number,
        onClick = { onNumberPressed(number) },
        modifier = modifier,
    )
}


@Composable
private fun OperationButton(
    operation: String,
    onOperationPressed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    RectangularButton(
        text = operation,
        textColor = MaterialTheme.colorScheme.primary,
        onClick = { onOperationPressed(operation) },
        modifier = modifier,
    )
}