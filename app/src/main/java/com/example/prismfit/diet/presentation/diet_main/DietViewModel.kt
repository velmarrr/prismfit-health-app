package com.example.prismfit.diet.presentation.diet_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.diet.data.model.Meal
import com.example.prismfit.diet.data.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _mealsFlow = MutableStateFlow<List<Meal>>(emptyList())
    val mealsFlow: StateFlow<List<Meal>> = _mealsFlow

    private val _mealToDelete = MutableStateFlow<String?>(null)
    val mealToDelete: StateFlow<String?> = _mealToDelete

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getAllMeals()
    }

    fun getAllMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            _mealsFlow.value = dietRepository.getAllMeals()
            _isLoading.value = false
        }
    }

    fun requestDeleteMeal(id: String) {
        _mealToDelete.value = id
    }

    fun confirmDelete() {
        val id = _mealToDelete.value
        if (id != null) {
            viewModelScope.launch {
                dietRepository.deleteMeal(id)
                _mealToDelete.value = null
                getAllMeals()
            }
        }
    }

    fun cancelDelete() {
        _mealToDelete.value = null
    }

    fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.getDefault(), "%.2f", value)
        }
    }
}