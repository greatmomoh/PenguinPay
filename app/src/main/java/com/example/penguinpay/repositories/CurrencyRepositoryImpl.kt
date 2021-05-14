package com.example.penguinpay.repositories

import com.example.penguinpay.remote.CurrencyWebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyWebService: CurrencyWebService
) : CurrencyRepository {

    override fun getExchangeRate(currencyCode: String): Flow<Double> {
        val hashMap: Map<String, Any?> = mapOf(
            "app_id" to "4933e62b90c34b49802b9a38b4c03945",
            "base" to "USD",
            "symbols" to currencyCode,
            "prettyprint" to true,
            "show_alternative" to true
        )

        return flow {
            val response = currencyWebService.fetchExchangeRate(hashMap)
            emit(response.rates[currencyCode] ?: 0.0)
        }

    }
}