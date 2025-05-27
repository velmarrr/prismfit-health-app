package com.example.prismfit.activity.presentation.activity_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.activity.domain.model.Activity
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.domain.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    private val selectedType = MutableStateFlow(ActivityType.WALKING)

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadActivities()
    }

    fun loadActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _activities.value = activityRepository.getActivities()
            _isLoading.value = false
        }
    }

    fun selectType(type: ActivityType) {
        selectedType.value = type
    }

    fun formatInstant(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    companion object {
        private const val DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
    }
}
