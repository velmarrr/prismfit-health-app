package com.example.prismfit.diet.presentation.add_diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.core.ui.utils.UiText
import com.example.prismfit.diet.data.model.Dish
import com.example.prismfit.diet.data.model.MealRequest
import com.example.prismfit.diet.data.repository.DietRepository
import com.example.prismfit.diet.presentation.add_diet.DishInput.CALORIES
import com.example.prismfit.diet.presentation.add_diet.DishInput.NAME
import com.example.prismfit.diet.presentation.add_diet.DishInput.PROTEIN
import com.example.prismfit.diet.presentation.add_diet.DishInput.WEIGHT
import com.example.prismfit.diet.presentation.add_diet.DishInput.FAT
import com.example.prismfit.diet.presentation.add_diet.DishInput.CARBS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddDietViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    private var mealId: String? = null

    fun initWithId(id: String?) {
        if (id == mealId) return
        mealId = id
        if (mealId != null) {
            viewModelScope.launch {
                val meal =  dietRepository.getAllMeals().find { it.id == mealId }
                meal?.let {
                    _state.value = ScreenState(
                        mealType = it.type,
                        dishes = it.dishes,
                        date = it.date
                    )
                }
            }
        }
    }

    fun onDishInputChange(input: DishInput, value: String) {
        val isValidNumber = value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*\$"))
        _state.update {
            when (input) {
                NAME -> it.copy(dishName = value)
                WEIGHT -> if (isValidNumber) it.copy(dishWeight = value) else it
                CALORIES -> if (isValidNumber) it.copy(dishCalories = value) else it
                PROTEIN -> if (isValidNumber) it.copy(dishProtein = value) else it
                FAT -> if (isValidNumber) it.copy(dishFat = value) else it
                CARBS -> if (isValidNumber) it.copy(dishCarbs = value) else it
            }
        }
    }

    fun onMealTypeChange(value: String) {
        _state.update {
            it.copy(mealType = value.take(20))
        }
    }

    fun addDish() {
        val state = _state.value
        if (state.dishName.isNotBlank() && state.dishWeight.toDoubleOrNull()?.let { it > 0.0 } == true) {
            val newDish =  Dish(
                name = state.dishName,
                weight = state.dishWeight.toDoubleOrNull() ?: 0.0,
                caloriesPer100 = state.dishCalories.toDoubleOrNull() ?: 0.0,
                proteinPer100 = state.dishProtein.toDoubleOrNull() ?: 0.0,
                fatPer100 = state.dishFat.toDoubleOrNull() ?: 0.0,
                carbsPer100 = state.dishCarbs.toDoubleOrNull() ?: 0.0
            )
            _state.update {
                it.copy(
                    dishes = it.dishes + newDish,
                    dishName = "",
                    dishWeight = "",
                    dishCalories = "",
                    dishProtein = "",
                    dishFat = "",
                    dishCarbs = ""
                )
            }
        } else if (state.dishName.isBlank()) {
            _state.update { it.copy(errorMessage = UiText.StringResource(R.string.dish_name_requirement)) }
        } else {
            _state.update { it.copy(errorMessage = UiText.StringResource(R.string.dish_weight_requirement)) }
        }
    }

    fun removeDish(dish: Dish) {
        _state.update {
            it.copy(
                dishes = it.dishes.filter { d -> d != dish }
            )
        }
    }

    fun save() {
        val mealType = _state.value.mealType.trim()
        if (mealType.isEmpty()) {
            _state.update { it.copy(errorMessage = UiText.StringResource(R.string.meal_type_requirement)) }
            return
        }
        if (_state.value.dishes.isEmpty()) {
            _state.update { it.copy(errorMessage = UiText.StringResource(R.string.added_dishes_requirement)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            dietRepository.saveMeal(
                MealRequest(
                    id = mealId,
                    type = mealType,
                    dishes = _state.value.dishes,
                    date = _state.value.date
                )
            )
            _exitChannel.send(Unit)
        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.getDefault(), "%.2f", value)
        }
    }

    data class ScreenState (
        val mealType: String = "",
        val dishes: List<Dish> = emptyList(),
        val isSaving: Boolean = false,
        val errorMessage: UiText? = null,
        val dishName: String = "",
        val dishWeight: String = "",
        val dishCalories: String = "",
        val dishProtein: String = "",
        val dishFat: String = "",
        val dishCarbs: String = "",
        val date: String? = null
    )
}