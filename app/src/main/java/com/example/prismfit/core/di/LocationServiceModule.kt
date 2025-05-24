package com.example.prismfit.core.di

import android.content.Context
import com.example.prismfit.activity.service.LocationServiceStarter
import com.example.prismfit.activity.service.impl.LocationServiceStarterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationServiceModule {

    @Provides
    fun provideLocationServiceStarter(
        @ApplicationContext context: Context
    ): LocationServiceStarter = LocationServiceStarterImpl(context)
}