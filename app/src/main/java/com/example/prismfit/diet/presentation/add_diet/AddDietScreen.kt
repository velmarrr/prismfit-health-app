package com.example.prismfit.diet.presentation.add_diet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.navigation.LocalNavController
import com.example.prismfit.notes.presentation.EventConsumer

@Composable
fun AddDietScreen(mealId: String?) {

    val viewModel: AddDietViewModel = hiltViewModel()

    LaunchedEffect(mealId) {
        viewModel.initWithId(mealId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    EventConsumer(viewModel.exitChannel) {
        navController.popBackStack()
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message.asString(context))
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AddDietContent(
            state = state,
            onMealTypeChange = viewModel::onMealTypeChange,
            onDishInputChange = viewModel::onDishInputChange,
            onAddDish = viewModel::addDish,
            onRemoveDish = viewModel::removeDish,
            onSave = viewModel::save,
            formatNumber = viewModel::formatNumber
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
