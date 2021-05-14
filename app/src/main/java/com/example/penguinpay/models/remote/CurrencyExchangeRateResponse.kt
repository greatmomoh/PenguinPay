package com.example.penguinpay.models.remote

data class CurrencyExchangeRateResponse(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Map<String, Double>,
    val timestamp: Int
)
