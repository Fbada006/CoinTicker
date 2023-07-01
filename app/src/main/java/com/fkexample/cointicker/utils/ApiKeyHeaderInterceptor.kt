package com.fkexample.cointicker.utils

import com.fkexample.cointicker.BuildConfig
import com.fkexample.cointicker.network.utils.HeaderInterceptor

/**
 * Utility object for creating an instance of API key header interceptor.
 */
object ApiKeyHeaderInterceptor {

    /**
     * Creates an instance of HeaderInterceptor with the specified header name and value.
     * @return An instance of HeaderInterceptor.
     */
    fun create() = HeaderInterceptor(headerName = "X-CoinAPI-Key", headerValue = BuildConfig.coinApiKey)
}
