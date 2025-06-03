package com.example.prismfit.home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prismfit.R
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.presentation.activity_main.components.ActivityItem
import com.example.prismfit.core.ui.theme.customColors
import com.example.prismfit.diet.domain.model.Meal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.prismfit.home.presentation.components.NutrientStat

@Composable
fun HomeContent(
    meals: List<Meal>,
    formatNumber: (Double) -> String,
    activities: List<Activity>,
    formatInstant: (Instant) -> String,
    onAction: (HomeAction) -> Unit
) {
    val today = LocalDate.now()
    val todayStart = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
    val todayEnd = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

    val todayMeals = meals.filter { it.date == today.toString() }
    val todayActivities = activities.filter { it.endTime >= todayStart && it.endTime < todayEnd }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val formattedDate = "${stringResource(R.string.today)}, ${today.format(formatter)}"
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.diet_screen),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAction(HomeAction.DietClick) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.customColors.greenContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        NutrientStat(
                            value = formatNumber(totalCalories),
                            unit = stringResource(R.string.calories_short),
                            label = stringResource(R.string.calories)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NutrientStat(
                            value = formatNumber(totalFat),
                            unit = stringResource(R.string.grams_short),
                            label = stringResource(R.string.fat)
                        )
                    }
                    Column {
                        NutrientStat(
                            value = formatNumber(totalProtein),
                            unit = stringResource(R.string.grams_short),
                            label = stringResource(R.string.protein)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NutrientStat(
                            value = formatNumber(totalCarbs),
                            unit = stringResource(R.string.grams_short),
                            label = stringResource(R.string.carbohydrates)
                        )
                    }
                }
            }
        }
        item {
            Text(
                text = stringResource(R.string.activity_main_screen),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
        }
        if (todayActivities.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.activities_absence),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(todayActivities) { activity ->
                ActivityItem(
                    color = MaterialTheme.customColors.purpleContainer,
                    activity = activity,
                    formatInstant = formatInstant,
                    onClick = { onAction(HomeAction.ActivityClick) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}