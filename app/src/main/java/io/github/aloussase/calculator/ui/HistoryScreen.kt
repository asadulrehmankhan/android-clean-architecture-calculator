package io.github.aloussase.calculator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryItemClicked
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    viewModel: CalculatorViewModel,
    navController: NavController,
    snackbar: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.history.isNotEmpty()) {
                FloatingActionButton(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = {
                        viewModel.onEvent(OnHistoryClear)
                        scope.launch {
                            snackbar.showSnackbar("History cleared")
                        }
                    },
                ) {
                    Icon(
                        Icons.Default.Delete,
                        "Clear history"
                    )
                }
            }

            LazyColumn(
                horizontalAlignment = Alignment.End,
            ) {
                items(state.history.size) { ix ->
                    val item = state.history[ix]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(OnHistoryItemClicked(item.content))
                                navController.navigate(Screen.Calculator)
                            },
                    ) {
                        Text(
                            text = item.content,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}