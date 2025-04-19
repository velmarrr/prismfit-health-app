package com.example.prismfit.core.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tokenStorage.accessTokenFlow.collect { token ->
                _isLoggedIn.value = !token.isNullOrBlank()
            }
        }
    }
}
