package com.example.prismfit.activity.presentation.activity_pending

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.activity.domain.model.ActivityType
import com.example.prismfit.activity.domain.model.NewActivity
import com.example.prismfit.activity.domain.model.toSerializable
import com.example.prismfit.activity.data.repository.ActivityRepository
import com.example.prismfit.activity.service.LocationService
import com.example.prismfit.activity.service.LocationServiceStarter
import com.example.prismfit.activity.service.ServiceActions
import com.example.prismfit.core.ui.utils.UiText
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class PendingActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    private val locationServiceStarter: LocationServiceStarter
) : ViewModel() {

    private val _path = MutableStateFlow<List<LatLng>>(emptyList())
    val path: StateFlow<List<LatLng>> = _path

    private val _distance = MutableStateFlow(0.0)
    val distance: StateFlow<Double> = _distance

    private val _pace = MutableStateFlow(0.0)
    val pace: StateFlow<Double> = _pace

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val _showBackDialog = MutableStateFlow(false)
    val showBackDialog: StateFlow<Boolean> = _showBackDialog

    private val _networkErrorMessage = MutableStateFlow<UiText?>(null)
    val networkErrorMessage: StateFlow<UiText?> = _networkErrorMessage

    private var startTime: Instant? = null
    private var timerJob: Job? = null

    fun startTracking() {
        LocationService.locationFlow.value = emptyList()
        _path.value = emptyList()
        startTime = Instant.now()
        _isTracking.value = true
        startTimer()
        locationServiceStarter.startService(ServiceActions.ACTION_START.name)
    }

    fun stopTracking(selectedType: ActivityType, onFinish: () -> Unit) {
        timerJob?.cancel()
        _isTracking.value = false
        val end = Instant.now()
        val distance = calculateTotalDistance(_path.value)
        val duration = Duration.between(startTime, end).seconds
        val newActivity = NewActivity(
            type = selectedType,
            startTime = startTime ?: return,
            endTime = end,
            distanceMeters = distance.toInt(),
            durationSeconds = duration,
            route = _path.value.map { it.toSerializable() }
        )
        viewModelScope.launch {
            try {
                repository.saveActivity(newActivity)
            } catch (e: Exception) {
                _networkErrorMessage.value = UiText.StringResource(R.string.save_error)
            } finally {
                onFinish()
            }
        }
        locationServiceStarter.startService(ServiceActions.ACTION_STOP.name)
        LocationService.locationFlow.value = emptyList()
    }

    fun updateLocation(locations: List<LatLng>) {
        _path.value = locations
        val totalDistance = calculateTotalDistance(locations)
        _distance.value = totalDistance.toDouble()
        if (_elapsedTime.value > 0 && totalDistance > 0.0) {
            _pace.value = (totalDistance / 1000.0) / (_elapsedTime.value / 3600.0)
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _elapsedTime.value += 1
            }
        }
    }

    private fun calculateTotalDistance(path: List<LatLng>): Float {
        var total = 0f
        for (i in 0 until path.size - 1) {
            val res = FloatArray(1)
            Location.distanceBetween(
                path[i].latitude, path[i].longitude,
                path[i + 1].latitude, path[i + 1].longitude,
                res
            )
            total += res[0]
        }
        return total
    }

    fun triggerBackDialog() {
        _showBackDialog.value = true
    }

    fun dismissBackDialog() {
        _showBackDialog.value = false
    }

    fun dismissNetworkError() {
        _networkErrorMessage.value = null
    }
}
