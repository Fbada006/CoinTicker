package com.fkexample.cointicker.utils

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Utility object for creating an instance of OkHttpClient for making HTTP requests.
 */
object HttpClient {

    /**
     * Creates and configures an OkHttpClient instance with the necessary interceptors and timeouts.
     * @return An instance of OkHttpClient.
     */
    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyHeaderInterceptor.create())
            .addInterceptor(LoggingInterceptor.create())
            .connectTimeout(API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(API_READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}
