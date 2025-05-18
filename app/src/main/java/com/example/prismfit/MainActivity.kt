package com.example.prismfit

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                   1001
                )
            }
        }

        setContent {
            AppTheme {
                CompositionLocalProvider(LocalSessionManager provides sessionManager) {
                    PrismFitApp()
                }
            }
        }
    }
}