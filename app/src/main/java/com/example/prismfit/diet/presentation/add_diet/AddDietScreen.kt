package com.example.prismfit.diet.presentation.add_diet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.R
import com.example.prismfit.diet.data.model.Dish
import com.example.prismfit.diet.presentation.add_diet.AddDietViewModel.*
import com.example.prismfit.diet.presentation.add_diet.components.DishInputField
import com.example.prismfit.navigation.LocalNavController
import com.example.prismfit.notes.presentation.EventConsumer

@Composable
fun AddDietScreen(mealId: String?) {

    val viewModel = hiltViewModel<AddDietViewModel, Factory> { factory ->
        factory.create(mealId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }

    EventConsumer(viewModel.exitChannel) {
        navController.popBackStack()
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDietContent(
    state: AddDietScreenUiState,
    onMealTypeChange: (String) -> Unit,
    onDishInputChange: (DishInput, String) -> Unit,
    onAddDish: () -> Unit,
    onRemoveDish: (Dish) -> Unit,
    onSave: () -> Unit,
    formatNumber: (Double) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        val mealTypes = listOf(stringResource(R.string.breakfast),
            stringResource(R.string.lunch), stringResource(R.string.dinner),
            stringResource(R.string.snack)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = state.mealType,
                onValueChange = onMealTypeChange,
                label = { Text(stringResource(R.string.meal_type)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                mealTypes.forEach { meal ->
                    DropdownMenuItem(
                        text = { Text(meal) },
                        onClick = {
                            onMealTypeChange(meal)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.added_dishes),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        state.dishes.forEach { dish ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(dish.name)
                        Text("${formatNumber(dish.weight)}g   " +
                                "${formatNumber(dish.totalCalories)}/" +
                                "${formatNumber(dish.totalProtein)}/" +
                                "${formatNumber(dish.totalFat)}/" +
                                "${formatNumber(dish.totalCarbs)}")
                    }
                    IconButton(
                        onClick = { onRemoveDish(dish) }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove_dish))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.add_new_dish),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        DishInputField(
            label = stringResource(R.string.dish_name),
            value = state.dishName,
            onValueChange = { onDishInputChange(DishInput.NAME, it) }
        )
        DishInputField(
            label = stringResource(R.string.weight_g),
            value = state.dishWeight,
            onValueChange = { onDishInputChange(DishInput.WEIGHT, it) },
            keyboardType = KeyboardType.Number
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DishInputField(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.calories_per_100),
                value = state.dishCalories,
                onValueChange = { onDishInputChange(DishInput.CALORIES, it) },
                keyboardType = KeyboardType.Number
            )
            DishInputField(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.protein_per_100),
                value = state.dishProtein,
                onValueChange = { onDishInputChange(DishInput.PROTEIN, it) },
                keyboardType = KeyboardType.Number
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DishInputField(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fat_per_100),
                value = state.dishFat,
                onValueChange = { onDishInputChange(DishInput.FAT, it) },
                keyboardType = KeyboardType.Number
            )
            DishInputField(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.carbs_per_100),
                value = state.dishCarbs,
                onValueChange = { onDishInputChange(DishInput.CARBS, it) },
                keyboardType = KeyboardType.Number
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onAddDish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_dish))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onSave,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text(stringResource(R.string.save_meal))
            }
        }
    }
}