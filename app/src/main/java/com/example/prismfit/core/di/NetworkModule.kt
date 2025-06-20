package com.example.prismfit.core.di

import com.example.prismfit.activity.data.network.api.ActivityApiService
import com.example.prismfit.auth.data.network.api.AuthApiService
import com.example.prismfit.auth.data.network.api.AuthInterceptor
import com.example.prismfit.diet.data.network.api.DietApiService
import com.example.prismfit.notes.data.network.api.NoteApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoteApi(retrofit: Retrofit): NoteApiService {
        return retrofit.create(NoteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDietApi(retrofit: Retrofit): DietApiService {
        return retrofit.create(DietApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideActivityApi(retrofit: Retrofit): ActivityApiService {
        return retrofit.create(ActivityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
