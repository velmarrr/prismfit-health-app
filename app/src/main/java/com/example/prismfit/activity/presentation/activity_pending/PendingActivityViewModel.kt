package com.example.prismfit.activity.presentation.activity_pending

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.activity.data.model.ActivityRequest
import com.example.prismfit.activity.data.model.toSerializable
import com.example.prismfit.activity.data.repository.ActivityRepository
import com.example.prismfit.activity.service.LocationService
import com.example.prismfit.activity.service.ServiceActions
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
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

    private var startTime: Instant? = null
    private var timerJob: Job? = null

    fun triggerBackDialog() {
        _showBackDialog.value = true
    }

    fun dismissBackDialog() {
        _showBackDialog.value = false
    }

    fun startTracking() {
        LocationService.locationFlow.value = emptyList()
        _path.value = emptyList()
        startTime = Instant.now()
        _isTracking.value = true
        startTimer()
        ContextCompat.startForegroundService(
            context,
            Intent(context, LocationService::class.java).apply {
                action = ServiceActions.ACTION_START.name
            }
        )
    }

    fun stopTracking(selectedType: String, onFinish: () -> Unit) {
        timerJob?.cancel()
        _isTracking.value = false
        val end = Instant.now()
        val distance = calculateTotalDistance(_path.value)
        val duration = Duration.between(startTime, end).seconds
        val request = ActivityRequest(
            type = selectedType,
            startTime = startTime!!,
            endTime = end,
            distanceMeters = distance.toInt(),
            durationSeconds = duration,
            route = _path.value.map { it.toSerializable() }
        )
        viewModelScope.launch {
            repository.saveActivity(request)
            onFinish()
        }
        ContextCompat.startForegroundService(
            context,
            Intent(context, LocationService::class.java).apply {
                action = ServiceActions.ACTION_STOP.name
            }
        )
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
}
