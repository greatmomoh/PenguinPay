package com.example.penguinpay.di

import com.example.penguinpay.BuildConfig
import com.example.penguinpay.remote.WebServiceClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideWebServiceFactory(
        webServiceClient: WebServiceClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        val baseClient = webServiceClient()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(baseClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}