package com.example.prismfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.core.session.SessionManager
import com.example.prismfit.core.session.TokenStorage
import com.example.prismfit.core.ui.PrismFitApp
import com.example.prismfit.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var tokenStorage: TokenStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                CompositionLocalProvider(LocalSessionManager provides sessionManager) {
                    PrismFitApp()
                }
            }
        }
    }
}