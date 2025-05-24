package com.example.prismfit.core.di

import android.content.Context
import com.example.prismfit.core.data.local.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    @Named("initial_locale")
    fun provideInitialLocale(@ApplicationContext context: Context): Locale {
        return context.resources.configuration.locales.get(0) ?: Locale.getDefault()
    }
}