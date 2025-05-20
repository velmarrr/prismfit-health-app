package com.example.prismfit.core.session

import androidx.compose.runtime.compositionLocalOf

val LocalSessionManager = compositionLocalOf<SessionManager> {
    error("No SessionManager provided")
}