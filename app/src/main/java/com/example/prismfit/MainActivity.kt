package com.example.prismfit

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.prismfit.core.data.local.DataStoreManager
import com.example.prismfit.core.session.LocalSessionManager
import com.example.prismfit.core.session.SessionManager
import com.example.prismfit.core.session.TokenStorage
import com.example.prismfit.core.ui.PrismFitApp
import com.example.prismfit.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
        val langCode = getPersistedLanguage(newBase)
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.setLocale(locale)
        val updatedContext: Context = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(updatedContext)
    }

    private fun getPersistedLanguage(context: Context): String {
        val dataStoreManager = DataStoreManager(context)
        return runBlocking {
            dataStoreManager.getPreferredLanguage().firstOrNull() ?: Locale.getDefault().language
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyPersistedLocale(this)

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

    private fun applyPersistedLocale(context: Context) {
        lifecycleScope.launch {
            val dataStoreManager = DataStoreManager(context)
            val preferredLanguage = dataStoreManager.getPreferredLanguage().firstOrNull()
            preferredLanguage?.let {
                val locale = Locale(it)
                Locale.setDefault(locale)
                val resources = context.resources
                val config = Configuration(resources.configuration)
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
            }
        }
    }
}