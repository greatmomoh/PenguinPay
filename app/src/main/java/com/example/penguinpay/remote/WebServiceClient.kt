package com.example.penguinpay.remote

import android.util.Log
import com.example.penguinpay.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebServiceClient @Inject constructor() {


    private val okHttpClient: OkHttpClient by lazy { buildOkHttpClient() }

    private fun buildOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ErrorInterceptor()) //always add this last, so okhttp executes it last
        .addInterceptor(makeLoggingInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    operator fun invoke(): OkHttpClient {
        return okHttpClient
    }


    class ErrorInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())

            response.headers.toMultimap().forEach {
                Log.d("Tag", it.toString())
            }

            if (response.isSuccessful.not()) { //https://www.restapitutorial.com/httpstatuscodes.html#:~:text=2xx%20Success,received%2C%20understood%2C%20and%20accepted.
                val responseBody = response.header("grpc-message")

                throw ServerErrorException(
                    responseBody ?: "There was an error handling your request, please retry."
                )
            }
            return response
        }

        class ServerErrorException(message: String) : IOException(message)
    }
}