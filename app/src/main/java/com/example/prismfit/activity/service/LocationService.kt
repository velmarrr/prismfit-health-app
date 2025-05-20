package com.example.prismfit.activity.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.prismfit.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class LocationService : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        val locationFlow = MutableStateFlow<List<LatLng>>(emptyList())
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ServiceActions.ACTION_START.name -> {
                startForegroundService()
                requestLocationUpdates()
            }
            ServiceActions.ACTION_STOP.name -> {
                stopForeground(true)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, "activity_channel")
            .setContentTitle(getString(R.string.prismfit_is_active_notification_header))
            .setContentText(getString(R.string.tracking_activity_notification))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(1, notification)
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateDistanceMeters(7f)
            .build()
        fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val newLocation = result.lastLocation ?: return

                if (newLocation.accuracy > 25f) return

                val newPoint = LatLng(newLocation.latitude, newLocation.longitude)
                val currentPoints = locationFlow.value

                val lastPoint = currentPoints.lastOrNull()
                if (lastPoint != null) {
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        lastPoint.latitude, lastPoint.longitude,
                        newPoint.latitude, newPoint.longitude,
                        results
                    )
                    if (results[0] < 7f) return
                }
                locationFlow.value = currentPoints + newPoint
            }
        }, Looper.getMainLooper())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "activity_channel",
            getString(R.string.activity_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.activity_channel_description)
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }
}
