package com.fkexample.cointicker.utils

import com.fkexample.cointicker.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Utility object for creating an instance of HttpLoggingInterceptor for logging HTTP requests and responses.
 */
object LoggingInterceptor {

    /**
     * Creates and configures an HttpLoggingInterceptor instance based on the build configuration.
     * @return An instance of HttpLoggingInterceptor.
     */
    fun create(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }
}
