package io.github.aloussase.calculator.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.aloussase.calculator.repository.NullCalculatorRepository
import io.github.aloussase.calculator.ui.CalculatorEvent.OnDeleteHistoryItem
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryClear
import io.github.aloussase.calculator.ui.CalculatorEvent.OnHistoryItemClicked
import io.github.aloussase.calculator.ui.theme.CalculatorTheme
import kotlinx.coroutines.launch

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HistoryScreenPreview() {
    val navController = rememberNavController()
    val snackbar = remember { SnackbarHostState() }
    val viewModel = CalculatorViewModel(NullCalculatorRepository())

    CalculatorTheme {
        HistoryScreen(
            viewModel,
            navController,
            snackbar
        )
    }
}


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
        Box {
            if (state.history.isNotEmpty()) {
                FloatingActionButton(
                    shape = RoundedCornerShape(50),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background,
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

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                LazyColumn(
                    horizontalAlignment = Alignment.End,
                ) {
                    items(
                        items = state.history,
                        key = { it.id!! }
                    ) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(OnHistoryItemClicked(item.content))
                                    navController.navigate(Screen.Calculator)
                                },
                        ) {
                            SwipeToDeleteItem(
                                item,
                                onDelete = {
                                    viewModel.onEvent(OnDeleteHistoryItem(it.id!!))
                                    scope.launch {
                                        snackbar.showSnackbar("Item deleted from history")
                                    }
                                }
                            ) {
                                Text(
                                    text = it.content,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SwipeToDeleteItem(
    item: T,
    onDelete: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                onDelete(item)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color = if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                MaterialTheme.colorScheme.error
            } else {
                Color.Transparent
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(16.dp),
            ) {
                Icon(
                    Icons.Default.Delete,
                    "Delete",
                    tint = Color.White,
                )
            }
        },
        dismissContent = {
            content(item)
        }
    )
}