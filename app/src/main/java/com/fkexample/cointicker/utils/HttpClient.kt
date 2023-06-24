package com.fkexample.cointicker.utils

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpClient {
    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyHeaderInterceptor.create())
            .addInterceptor(LoggingInterceptor.create())
            .connectTimeout(API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(API_READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}