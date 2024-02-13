package io.github.aloussase.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.aloussase.calculator.ui.AboutScreen
import io.github.aloussase.calculator.ui.CalculatorScreen
import io.github.aloussase.calculator.ui.CalculatorViewModel
import io.github.aloussase.calculator.ui.HistoryScreen
import io.github.aloussase.calculator.ui.Screen
import io.github.aloussase.calculator.ui.theme.CalculatorTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {

                var showOptionsMenu by remember { mutableStateOf(false) }
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {},
                            actions = {
                                IconButton(onClick = { showOptionsMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Options"
                                    )
                                }

                                OptionsMenu(
                                    navController,
                                    expanded = showOptionsMenu,
                                    onDismiss = { showOptionsMenu = false }
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CalculatorRoutes
                    ) {
                        navigation(
                            startDestination = Screen.Calculator,
                            route = Screen.CalculatorRoutes
                        ) {
                            composable(Screen.Calculator) { entry ->
                                val viewModel = entry.viewModel<CalculatorViewModel>(navController)
                                CalculatorScreen(
                                    viewModel,
                                    snackbarHostState,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            composable(Screen.About) {
                                AboutScreen(
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            composable(Screen.History) { entry ->
                                val viewModel = entry.viewModel<CalculatorViewModel>(navController)
                                HistoryScreen(
                                    viewModel,
                                    navController,
                                    snackbarHostState,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) { navController.getBackStackEntry(navGraphRoute) }
    val viewModel = hiltViewModel<T>(parentEntry)
    return viewModel
}

@Composable
private fun OptionsMenu(
    navController: NavController,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onDismiss: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        modifier = modifier,
        onDismissRequest = { onDismiss() }
    ) {
        DropdownMenuItem(
            text = { Text(text = "About") },
            onClick = {
                Log.d("MainActivity", "About clicked")
                navController.navigate(Screen.About)
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text(text = "History") },
            onClick = {
                Log.d("MainActivity", "History clicked")
                navController.navigate(Screen.History)
                onDismiss()
            }
        )
    }
}