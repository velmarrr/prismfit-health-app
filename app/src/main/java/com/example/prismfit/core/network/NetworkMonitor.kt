package com.example.prismfit.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.google.firebase.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkChecker = NetworkChecker()
    private var monitoringJob: Job? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            startInternetMonitoring()
        }

        override fun onUnavailable() {
            _isConnected.value = false
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        startInternetMonitoring()
    }

    private fun startInternetMonitoring() {
        if (monitoringJob?.isActive == true) {
            return
        }

        networkChecker.startChecking()

        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            networkChecker.isInternetAvailable.collect { available ->
                _isConnected.value = available
            }
        }
    }

    fun cleanup() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.w("NetworkMonitor", "Callback is already unregistered or not registered yet. ", e)
            }
        }

        monitoringJob?.cancel()
        monitoringJob = null
        networkChecker.stopChecking()
    }
}