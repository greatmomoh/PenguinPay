package com.example.penguinpay.remote

import com.example.penguinpay.repositories.CurrencyRepository
import com.example.penguinpay.repositories.CurrencyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindCurrencyRepositoryImpl(repository: CurrencyRepositoryImpl): CurrencyRepository

}

@Module
@InstallIn(SingletonComponent::class)
object Providers {

    @Provides
    @Singleton
    fun provideCurrencyWebService(retrofit: Retrofit): CurrencyWebService {
        return retrofit.create()
    }

}