package com.example.prismfit.core.network

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected

    override fun onCleared() {
        super.onCleared()
        networkMonitor.cleanup()
    }
}