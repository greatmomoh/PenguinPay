package com.example.penguinpay.repositories

import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun getExchangeRate(currencyCode: String): Flow<Double>

}