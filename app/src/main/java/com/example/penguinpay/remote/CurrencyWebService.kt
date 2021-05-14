package com.example.penguinpay.remote

import com.example.penguinpay.models.remote.CurrencyExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyWebService {
    @GET("latest.json")
    suspend fun fetchExchangeRate(
        @QueryMap body: Map<String, @JvmSuppressWildcards Any?>,
    ): CurrencyExchangeRateResponse
}