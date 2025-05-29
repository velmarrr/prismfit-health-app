package com.example.prismfit.diet.presentation.diet_main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prismfit.R
import com.example.prismfit.diet.domain.model.Meal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DietContent(
    meals: List<Meal>,
    mealToDelete: String?,
    onAction: (DietAction) -> Unit,
    formatNumber: (Double) -> String
) {

    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val todayMeals = meals.filter { it.date == todayDate }
    val totalCalories = todayMeals.sumOf { meal ->
        meal.dishes.sumOf { it.totalCalories }
    }
    val totalProtein = todayMeals.sumOf { meal ->
        meal.dishes.sumOf { it.totalProtein }
    }
    val totalFat = todayMeals.sumOf { meal ->
        meal.dishes.sumOf { it.totalFat }
    }
    val totalCarbs = todayMeals.sumOf { meal ->
        meal.dishes.sumOf { it.totalCarbs }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, end = 16.dp, start = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row {
                        Text(
                            text = formatNumber(totalCalories),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " " + stringResource(R.string.calories_short),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                    Text(stringResource(R.string.calories))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = formatNumber(totalFat),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " " + stringResource(R.string.grams_short),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                    Text(stringResource(R.string.fat))
                }
                Column {
                    Row {
                        Text(
                            text = formatNumber(totalProtein),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " " + stringResource(R.string.grams_short),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                    Text(stringResource(R.string.protein))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = formatNumber(totalCarbs),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " " + stringResource(R.string.grams_short),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                    Text(stringResource(R.string.carbohydrates))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            val groupedByDate = meals
                .sortedByDescending { it.date }
                .groupBy { it.date }
            groupedByDate.forEach { (date, meals) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(meals) { meal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onAction(DietAction.MealClick(meal.id)) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                val displayMealType = when (meal.type) {
                                    "breakfast" -> stringResource(R.string.breakfast)
                                    "lunch" -> stringResource(R.string.lunch)
                                    "dinner" -> stringResource(R.string.dinner)
                                    "snack" -> stringResource(R.string.snack)
                                    else -> meal.type
                                }
                                Text(
                                    text = displayMealType,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                meal.dishes.forEach { dish ->
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(dish.name, fontWeight = FontWeight.Bold)
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            formatNumber(dish.weight) +
                                                    stringResource(R.string.grams_short) + "  " +
                                                    "${formatNumber(dish.totalCalories)}/" +
                                                    "${formatNumber(dish.totalProtein)}/" +
                                                    "${formatNumber(dish.totalFat)}/" +
                                                    formatNumber(dish.totalCarbs)
                                        )
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                            IconButton(onClick = { onAction(DietAction.DeleteRequest(meal.id)) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
        if (mealToDelete != null) {
            AlertDialog(
                onDismissRequest = { onAction(DietAction.DeleteCancel) },
                title = { Text(stringResource(R.string.delete_confirmation)) },
                text = { Text(stringResource(R.string.meal_delete_confirmation_question)) },
                confirmButton = {
                    TextButton(onClick = { onAction(DietAction.DeleteConfirm) }) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(DietAction.DeleteCancel) }) {
                        Text(stringResource(R.string.no))
                    }
                }
            )
        }
    }
}