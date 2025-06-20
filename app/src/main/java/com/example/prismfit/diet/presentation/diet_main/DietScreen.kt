package com.example.prismfit.diet.presentation.diet_main

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun DietScreen(onMealClick: (String) -> Unit) {

    val viewModel: DietViewModel = hiltViewModel()
    val meals by viewModel.mealsFlow.collectAsStateWithLifecycle()
    val mealToDelete by viewModel.mealToDelete.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val networkErrorMessage by viewModel.networkErrorMessage.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(networkErrorMessage) {
        networkErrorMessage?.let {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_LONG).show()
            viewModel.dismissNetworkError()
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getAllMeals()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            DietContent(
                meals = meals,
                mealToDelete = mealToDelete,
                onAction = { action ->
                    when (action) {
                        is DietAction.DeleteRequest -> viewModel.requestDeleteMeal(action.mealId)
                        DietAction.DeleteConfirm -> viewModel.confirmDelete()
                        DietAction.DeleteCancel -> viewModel.cancelDelete()
                        is DietAction.MealClick -> onMealClick(action.mealId)
                    }
                },
                isConnected = isConnected,
                formatNumber = viewModel::formatNumber
            )
        }
    }
}
