package com.example.prismfit

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prismfit.core.data.local.DataStoreManager
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.core.session.SessionManager
import com.example.prismfit.core.session.TokenStorage
import com.example.prismfit.core.ui.PrismFitApp
import com.example.prismfit.core.ui.theme.AppTheme
import com.example.prismfit.core.ui.theme.ThemePreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var tokenStorage: TokenStorage

    override fun attachBaseContext(newBase: Context) {
        val dataStoreManager = DataStoreManager(newBase)
        val langCode = runBlocking {
            dataStoreManager.ensurePreferredLanguageInitialized()
            dataStoreManager.getPreferredLanguage().firstOrNull() ?: Locale.getDefault().language
        }
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.setLocale(locale)
        val updatedContext = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRuntimePermission()

        setContent {
            val context = LocalContext.current
            val dataStoreManager = remember { DataStoreManager(context) }
            var themePreference by remember { mutableStateOf<ThemePreference?>(null) }
            LaunchedEffect(Unit) {
                dataStoreManager.getThemePreference().collect { pref ->
                    themePreference = pref
                }
            }
            themePreference?.let { theme ->
                AppTheme(themePreference = theme) {
                    CompositionLocalProvider(LocalSessionManager provides sessionManager) {
                        PrismFitApp()
                    }
                }
            }
        }
    }

    private fun checkRuntimePermission() {
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
    }
}