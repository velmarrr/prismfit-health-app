package com.example.prismfit.activity.service.impl

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.prismfit.activity.service.LocationService
import com.example.prismfit.activity.service.LocationServiceStarter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationServiceStarterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationServiceStarter {
    override fun startService(action: String) {
        val intent = Intent(context, LocationService::class.java).apply {
            this.action = action
        }
        ContextCompat.startForegroundService(context, intent)
    }
}