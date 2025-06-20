package com.example.prismfit.core.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor() {
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var checkingJob: Job? = null

    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable.asStateFlow()

    fun startChecking() {
        if (checkingJob?.isActive == true) {
            return
        }

        checkingJob = scope.launch {
            while (isActive) {
                val reachable = try {
                    val url = URL("https://clients3.google.com/generate_204")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 1000
                    connection.readTimeout = 1000
                    connection.connect()
                    connection.responseCode == 204
                } catch (e: Exception) {
                    false
                }
                _isInternetAvailable.value = reachable
                delay(2000)
            }
        }
    }

    fun stopChecking() {
        checkingJob?.cancel()
        checkingJob = null
        scope.cancel()
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}
